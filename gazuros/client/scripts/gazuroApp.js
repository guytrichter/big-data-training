var app = angular.module('gazuroApp', []);

// Controllers
app.controller('projectController', function($scope, $window, $location, dataService) {
	$scope.user = {
		firstName: 'Charlie'
	}
	
	$scope.projects = [{"id":32,"name":"pot","price":0.0,"provider":"jiffy","description":"pots"},{"id":42,"name":"soil pellet","price":0.0,"provider":"jiffy","description":"soil pellet"},{"id":52,"name":"plant marker","price":0.0,"provider":"mastertag","description":"plant markers"},{"id":62,"name":"polybag for soil","price":0.0,"provider":"amazon","description":"polybag for soil"},{"id":72,"name":"master carton","price":0.0,"provider":"office depot","description":"master carton - can hold up to 24 units"},{"id":82,"name":"bonsai instructions","price":0.0,"provider":"china","description":"bonsai instructions"},{"id":92,"name":"bonsai packaging box","price":0.0,"provider":"china","description":"bonsai packaging boxe"},{"id":102,"name":"jacaranda seed packet","price":0.0,"provider":"page seed","description":"jacranda packet"},{"id":112,"name":"Delonix seed packet","price":0.0,"provider":"page seed","description":"Delonix seed packet"},{"id":122,"name":"Picea seed packet","price":0.0,"provider":"page seed","description":"Picea seed packet"},{"id":132,"name":"Pinus seed packet","price":0.0,"provider":"page seed","description":"Pinus seed packet"},{"id":142,"name":"herb instructions","price":0.0,"provider":"china","description":"herbs instructions"},{"id":152,"name":"herb packaging box","price":0.0,"provider":"china","description":"herb packaging box"},{"id":162,"name":"basil","price":0.0,"provider":"page seed","description":"basil seed packet"},{"id":172,"name":"sage","price":0.0,"provider":"page seed","description":"sage seed packet"},{"id":182,"name":"parsley","price":0.0,"provider":"page seed","description":"parsley seed packet"},{"id":192,"name":"thyme","price":0.0,"provider":"page seed","description":"thyme seed packet"},{"id":202,"name":"cilantro","price":0.0,"provider":"page seed","description":"cilantro seed packet"},{"id":212,"name":"Crazy garden instructions","price":0.0,"provider":"china","description":"Crazy garden instructions"},{"id":222,"name":"Crazy garden packaging box","price":0.0,"provider":"china","description":"Crazy garden packaging box"},{"id":232,"name":"Purple carrot","price":0.0,"provider":"page seed","description":"Purple carrot seed packet"},{"id":242,"name":"Black corn","price":0.0,"provider":"page seed","description":"Black corn seed packet"},{"id":252,"name":"Lemon cucumber","price":0.0,"provider":"page seed","description":"Lemon cucumber seed packet"},{"id":262,"name":"5 color swiss chard","price":0.0,"provider":"page seed","description":"5 Color swiss chard seed packet"},{"id":272,"name":"Romanesco Brocolli","price":0.0,"provider":"page seed","description":"Romanesco Brocolli seed packet"}]

	// dataService.getAllProjects().then(function(response) {
		// console.log($scope);
		// console.log(response);
		// $scope.projects = response.data;
		// $scope.user.totalMsgCount = $scope.projects.length;
		
		// // $scope.user.unreadMsgCount = function() {
			// // var output = 0;
			// // for (var i = 0; i < $scope.messages.length; i++) {
				// // if ($scope.messages[i] && $scope.messages[i].isRead == false) {
					// // output++;
				// // }
			// // }
			// // return output;
		// // }();
	// });

	$scope.openMsg = function(i_msg) {
		if (typeof(i_msg) != 'undefined' && i_msg != null) {
			var url = "https://" + $window.location.host + "/message.html#/?msgID=" + i_msg._id;
			$window.location.href = url;
		}
		else {
			console.log("Error loading msg: " + i_msg);
		}
	}

	$scope.goToInbox = function() {
		var url = "https://" + $window.location.host + "/inbox.html";
		$window.location.href = url;
	}
});

app.controller('inventoryController', function($scope, $window, $location, dataService) {
	$scope.currentInventory = [{"id":2,"productId":72,"count":5000,"lastUpdate":1483024102902,"packager":"NY"}];
	
	$scope.goToMainMenu = function() {
		var url = "file:///C:/Users/Oren/Documents/Git%20Coding%20Projects/Gazuros/client/menu.html#/";
		$window.location.href = url;
	}
	
});

app.controller('menuController', function($scope, $window, $location, dataService) {
	$scope.showPrompt = false;
	$scope.showOrderPrompt = false;
	$scope.confirmShip = false;
	
	$scope.currentMenu = "menu_1";
	
	$scope.currentOrder = {
		numberOfBoxes : 0,
		unitsPerBox : 0
	};
	
	// stubs
	$scope.backorders = [{info: '5000 turtlenecks'}, {info: '35000 neckturtles'}, {info: '12 yamalkes'}];
	$scope.outgoingkits = [{info: 'Bonzai'}, {info: 'Crazy Garden'}, {info: 'Herbs'}]; 
	
	$scope.initMenu = function() {
		$scope.currentMenu = "menu_1";

	}();
	
	$scope.shouldShow = function(i_menuNumber) {
		return $scope.currentMenu == i_menuNumber;
	}
	
	$scope.goToMenu = function(i_menuNumber, i_additionalParams) {
		$scope.currentMenu = 'menu_' + i_menuNumber;
	}
	
	// Confirmations & Prompts
	$scope.confirmRecieve = function(i_order) {
		if (i_order && !!i_order.info) {
			$scope.itemToConfirm = i_order.info;
			$scope.showPrompt = true;
		}
	}
	
	$scope.verifyBeforeOrder = function() {
		$scope.confirmShip = true;
	}
	
	$scope.placeOrder = function(i_order) {
		if (i_order && !!i_order.info) {
			$scope.selectedKit = i_order.info;
			$scope.showOrderPrompt = true;
		}
	}
	
	$scope.hidePrompt = function() {
		$scope.showPrompt = false;
		$scope.showOrderPrompt = false;
		$scope.confirmShip = false;
	}
	
	$scope.showModalPrompt = function() {
		$scope.showPrompt = true;
	}
	
	// Navigation 
	$scope.goToInventoryPage = function() {
		var url = "file:///C:/Users/Oren/Documents/Git%20Coding%20Projects/Gazuros/client/inventory.html#/";
		$window.location.href = url;
	}
	
	$scope.goToProductPage = function() {
		var url = "https://" + $window.location.host + "/products.html";
		$window.location.href = url;
	}
	
	
});

// Services

app.service('dataService', function($http) {
	this.getAllProjects = function() {
		return $http.get("http://gazuros-app.cfapps.io/products/getProducts").then(function(response) {
			return response;
		});
	}

	// this.getMail = function() {
		// return $http.get("https://medallia-test-om02.c9users.io/api/mail").then(function(response) {
			// return response;
		// });
	// }

	// this.markMessageRead = function(i_msgID) {
		// return $http.put("https://medallia-test-om02.c9users.io/api/mail/" + i_msgID, {
			// isRead: true
		// }).then(function(response) {
			// return response;
		// });
	//}
});

// Directives
// app.directive("mailBox", function() {
// 	return {
// 		restrict: "E",
// 		templateUrl: "./mailBoxTemplate.html",
// 		scope: {
// 			messages: '=',
// 			openMsg: '='
// 		}
// 	};
// });

// app.directive("singleMessage", ['$location', 'mailService', function($location, mailService) {
	// return {
		// restrict: "E",
		// templateUrl: "./singleMsgTemplate.html",
		// scope: {
			// msg: '='
		// },
		// link: function(scope, element, attrs) {
			// var msgID = $location.search()['msgID'];
			// mailService.getLetterByID(msgID).then(function(response) {
				// scope.msg = response.data;
			// });

			// // Entering a message means it is read
			// mailService.markMessageRead(msgID);
		// }
	// };
// }]);
