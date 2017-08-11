package me.ckhd.opengame.online.util.anzhi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationProblemHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;


@SuppressWarnings("deprecation")
public class JacksonMapper {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	public JacksonMapper() {
	}
	
	public static ObjectMapper getInstance() {
		return mapper;
	}

	static {
/*		JacksonMapper.addHandler(new DeserializationProblemHandler() {
			public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
				System.out.println(beanOrClass + "  unknown Property-->" + propertyName);
				return false;
			}
		});*/
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, true);
		mapper.getDeserializationConfig();
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, false);
	}

	public static void addHandler(DeserializationProblemHandler deserializationProblemHandler) {
		mapper.getDeserializationConfig().addHandler(deserializationProblemHandler);
	}

	public static <T> T readValue(String jsonStr, Class<T> clazz) {
		try {
			T t = mapper.readValue(jsonStr, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * json 字符串 转换成 List对象
	 * @param <T>
	 * @param json
	 * @param tClazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static <T> List<T> readListValue(String json, Class<T> tClazz){
		return readValue(json,java.util.List.class,tClazz);
	}

	/**
	 * Json 字符串 转换成Collection对象
	 * @param <U>
	 * @param <T>
	 * @param json
	 * @param uClazz
	 * @param tClazz
	 * @return
	 */
	public static <U extends Collection<T>,T> U readValue(String json, Class<U> uClazz, Class<T> tClazz){
		try {
			return mapper.readValue(json, TypeFactory.collectionType(uClazz,tClazz));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * json 转换成Map  zhangzhi
	 * @param <T>
	 * @param json
	 * @param tClazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static <T> Map<String,T> readMapValue(String json, Class<T> tClazz){
		return readMapValue(json,java.util.Map.class,tClazz);
	}
	
	/**
	 * json 转换成Map 
	 * @param <U>
	 * @param <T>
	 * @param json
	 * @param uClazz
	 * @param tClazz
	 * @return
	 */
	public static <U extends Map<String,T>,T> U readMapValue(String json, Class<U> uClazz, Class<T> tClazz){
		try {
			return mapper.readValue(json, TypeFactory.mapType(uClazz, java.lang.String.class, tClazz));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	

	public static String getJsonFromObject(Object t) {
		try {
			String temp = mapper.writeValueAsString(t);
			return temp=temp.replace("\"","'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static String getJsonFromEntity(Object t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static String toJsonFilter(Object obj, String field) {
		try {
			String str = JacksonMapper.getJsonFromObject(obj);
			if (null == str) {
				return "application error, no data";
			}
			String[] fs = null;
			if (field == null || field.isEmpty()) {
				return str;
			} else {
				fs = field.split(",");
			}
			HashMap<String, Object> map = readValue(str, HashMap.class);
			Map<String, Object> newMap = new HashMap<String, Object>();
			for (String value : fs) {
				newMap.put(value, map.get(value));
			}
			return mapper.writeValueAsString(newMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "application error, no data";
	}
	
	public static void main(String[] args) {
		System.out.println(JacksonMapper.readMapValue("{\"id\":1,\"loginname\":\"aaa\"}", Object.class));
	}

	
}
