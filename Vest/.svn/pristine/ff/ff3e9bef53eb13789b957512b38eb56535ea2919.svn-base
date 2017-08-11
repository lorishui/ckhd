// 数字比较
function compareNumber(val1, val2) {
	try{
		if(val1 == 'N/A'){
			val1 = 0;
		}
	}catch(e){
	}
	try{
		if(val2 == 'N/A'){
			val2 = 0;
		}
	}catch(e){
	}
	return val1 - val2;
}
// 百分比比较
function compareRate(val1, val2) {
	val1 = $.trim(val1);
	val2 = $.trim(val2);
	var index = val1.indexOf(".");
	if (index == 1) {
		val1 = "00" + val1;
	} else if (index == 2) {
		val1 = "0" + val1;
	}
	index = val2.indexOf(".");
	if (index == 1) {
		val2 = "00" + val2;
	} else if (index == 2) {
		val2 = "0" + val2;
	}

	return val1.localeCompare(val2);
}