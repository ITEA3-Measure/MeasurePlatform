

angular.module('measurePlatformApp').directive('goClick', ['$window', function(window){

 return {
  link: function (scope, element, attributes) {
	    var path;

	    attributes.$observe( 'goClick', function (val) {
	      path = val;
	    });

	    element.bind( 'click', function () {
	      scope.$apply( function () {
	    	window.open(path, '_blank');
	      });
	    });
	}
 };
}]);
