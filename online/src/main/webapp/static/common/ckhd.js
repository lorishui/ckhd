(function( window, undefined ) {

	//fix jquery clone bug
	(function (original) {
		jQuery.fn.clone = function () {
			var $this = this;
			var $that = original.apply($this, arguments);
			jQuery.each(['textarea', 'select'], function(i, item){
				var $s = $this.find(item).add($this.filter(item));
				var $t = $that.find(item).add($that.filter(item));
				$s.each(function(i, item){
					jQuery($t.get(i)).val(jQuery(item).val());
				});
			});
			return $that;
		};
	}) (jQuery.fn.clone);

})( window );
