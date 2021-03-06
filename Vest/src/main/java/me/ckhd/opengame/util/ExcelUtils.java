package me.ckhd.opengame.util;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author wizard
 *
 */
public class ExcelUtils {
	
	public static void write(InputStream is, OutputStream os, Config config, List<Map<String, Object>> data) throws IOException {
		try {
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFSheet sheet = StringUtils.isBlank(config.getSheetName()) ? wb.getSheetAt(0) : wb.getSheet(config.getSheetName());
			
			for(int i=0; i<data.size(); i++) {
				Map<String, Object> rd = data.get(i);
				XSSFRow row = sheet.createRow(config.getDataRowNumber() + i);
				for(Column column : config.getColumns()){
					Object val = rd.get(column.getName());
					if( val != null ) {
						XSSFCell cell = row.createCell(column.getIndex());
						setCellVal(cell, val);
					}
				}
			}
			
			for(Column column : config.getColumns()){
				if( column.isHidden() ) {
					sheet.setColumnHidden(column.getIndex(), column.isHidden());
				}
			}
			
			wb.write(os);
		}
		finally {
			close(is);
			close(os);
		}
	}
	
	private static void setCellVal(XSSFCell cell, Object val) {
		if( val == null ) {
			return;
		}
		if( val.getClass().isAssignableFrom(Boolean.class) ) {
			cell.setCellValue( (Boolean)val );
		}
		else if( val.getClass().isAssignableFrom(Calendar.class) ) {
			cell.setCellValue((Calendar)val);
		}
		else if( val.getClass().isAssignableFrom(Date.class) ) {
			cell.setCellValue((Date)val);
		}
		else if( val.getClass().isAssignableFrom(Double.class) ) {
			cell.setCellValue((Double)val);
		}
		else if( val.getClass().isAssignableFrom(RichTextString.class) ) {
			cell.setCellValue((RichTextString)val);
		}
		else {
			cell.setCellValue(val.toString());
		}
	}

	
	public static interface IConvert<T> {
		public void bean2map(T bean, Map<String, Object> map);
	}
	public static <T>Map<String, Object> bean2map(T bean, IConvert<T> convert) {
		Map<String, Object> ret = new Gson().fromJson(new Gson().toJson(bean), new TypeToken<Map<String, Object>>() {}.getType());
		if( convert != null ) {
			convert.bean2map(bean, ret);
		}
		return ret;
	}
	public static <T>List<Map<String, Object>> bean2maps(List<T> beans, IConvert<T> convert) {
		List<Map<String, Object>> rets = new ArrayList<Map<String, Object>>();
		for(T bean : beans) {
			rets.add(bean2map(bean, convert));
		}
		return rets;
	}
	
	public static class Config {
		private String sheetName;
		private Integer dataRowNumber;
		private List<Column> columns;
		public Integer getDataRowNumber() {
			return dataRowNumber;
		}
		public Config setDataRowNumber(Integer dataRowNumber) {
			this.dataRowNumber = dataRowNumber;
			return this;
		}
		public List<Column> getColumns() {
			return columns;
		}
		public Config setColumns(List<Column> columns) {
			this.columns = columns;
			return this;
		}
		public String getSheetName() {
			return sheetName;
		}
		public Config setSheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}
	}
	public static class Column {
		private String name;
		private String alias;
		private int index;
		private boolean hidden = false;
		public String getName() {
			return name;
		}
		public Column setName(String name) {
			this.name = name;
			return this;
		}
		public String getAlias() {
			return alias;
		}
		public Column setAlias(String alias) {
			this.alias = alias;
			return this;
		}
		public int getIndex() {
			return index;
		}
		public Column setIndex(int index) {
			this.index = index;
			return this;
		}
		public boolean isHidden() {
			return hidden;
		}
		public Column setHidden(boolean hidden) {
			this.hidden = hidden;
			return this;
		}
	}
	
	public static void close(Closeable closeable){
		try {
			if( closeable != null ) {
				closeable.close();
			}
		}
		catch(Exception e){}
	}
	
	public static void main(String argc[]) throws IOException {
		
		InputStream is = null;
		OutputStream os = null;
		
		try {
			List<Column> cols = new ArrayList<Column>();
			cols.add(new Column().setIndex(0).setName("a1"));
			cols.add(new Column().setIndex(1).setName("a2"));
			cols.add(new Column().setIndex(2).setName("a3"));
			
			Config config = new Config().setDataRowNumber(1).setColumns(cols);
			
			is = new FileInputStream("z:/创酷导出数据需求模板(1).xlsx");
			
			os = new FileOutputStream("z:/a.xlsx");
			
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object> row = Maps.newHashMap();
			row.put("a1", "1");
			row.put("a2", "2");
			row.put("a3", "3");
			data.add(row);
			
			write(is, os, config, data);
		}
		finally {
			close(is);
			close(os);
		}
	}
}
