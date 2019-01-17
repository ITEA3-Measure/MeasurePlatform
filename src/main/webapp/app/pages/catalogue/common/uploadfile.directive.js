(function() {
	'use strict';

	angular.module('measurePlatformApp').directive('fileread', fileread);

	function fileread() {
		var directive = {
			restrict : 'A',
			link : linkFunc
		};

		return directive;

		function linkFunc(scope, element, attributes) {
			element.bind("change", function(changeEvent) {
				var reader = new FileReader();
				reader.onload = function(loadEvent) {
					scope.$apply(function() {
						scope.fileread = loadEvent.target.result;
					});
				}
				reader.readAsDataURL(changeEvent.target.files[0]);
				scope.myFile = element[0].files[0];
			});
		}
	}

})();



(function() {
	'use strict';

	angular.module('measurePlatformApp').directive('fileModel', fileModel);
	function fileModel() {
		var directive = {
			restrict : 'A',
			link : linkFunc2
		};

		function linkFunc2(scope, element, attributes) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					scope.myFile = element[0].files[0];
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	}
})();
