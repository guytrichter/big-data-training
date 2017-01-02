var app = angular.module('gazuroApp', []);
var host_test = "127.0.0.1:8080";
var host = "gazuros-app.cfapps.io";

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

});

app.controller('inventoryController', function($scope, $window, $location, dataService) {
	$scope.currentInventory = [{"id":2,"productId":72,"count":5000,"lastUpdate":1483024102902,"packager":"NY"}];

	$scope.currentInvetory2 = {"Inventory{id=2, productId=72, count=5000, lastUpdate=1483024102902, packager=NY}":{"id":72,"name":"master carton","price":0.0,"provider":"office depot","description":"master carton - can hold up to 24 units"}}

	// Dangerzone
	function fixFormat(i_deformedObj) {
		if (typeof(i_deformedObj) != 'undefined' && i_deformedObj != null) {
			var fixedOutput = Object.keys(i_deformedObj)[0].replace("Inventory", "").replace(/=/g, ":").replace(/packager:/, "packager:'").replace("}", "'}");
			return eval('(' + fixedOutput + ')');
		}
	}



	$scope.goToMainMenu = function() {
		var url = "menu.html";
		$window.location.href = url;
	}

});

app.controller('menuController', function($scope, $http, $window, $location, dataService) {
	$scope.showPrompt = false;
	$scope.showOrderPrompt = false;
	$scope.confirmShip = false;
	$scope.showProductPrompt = false;

	$scope.currentMenu = "menu_1";

	$scope.currentOrder = {
		numberOfBoxes : 0,
		unitsPerBox : 0
	};

	$scope.testServer = function() {
		return $http.get("http://" + host + "/products/getProducts").then(function(response) {
			console.log(response);
		});
	}

	$scope.order = {};

	// stubs
	$scope.backorders = [{"id":22,"productId":42,"dateTimestamp":1483023905926,"dateStr":"2016-12-29T15:05:05.926Z","amount":5000,"status":"BACK_ORDER","shippingPrice":0.0,"packager":"NY"},{"id":32,"productId":52,"dateTimestamp":1483023926733,"dateStr":"2016-12-29T15:05:26.733Z","amount":5000,"status":"BACK_ORDER","shippingPrice":0.0,"packager":"NY"}];
	$scope.outgoingkits = ["BONSAI","HERBS","CRAZY_GARDEN"];
	
	// Get Data
	dataService.getKitNames().then(function(response) {
		if (response && response.data) {
			$scope.outgoingkits = response.data;
		}
	});
	dataService.getBackOrders().then(function(response) {
		if (response && response.data) {
			$scope.backorders = response.data;
		}
	});
	

	$scope.initMenu = function() {
		$scope.currentMenu = "menu_1";
	}();

	$scope.shouldShow = function(i_menuNumber) {
		return $scope.currentMenu == i_menuNumber;
	}

	$scope.goToMenu = function(i_menuNumber, i_additionalParams) {
		$scope.currentMenu = 'menu_' + i_menuNumber;
	}

	$scope.showProductOrderForm = function() {
		$scope.showProductPrompt = true;
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
		$scope.showProductPrompt = false;
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

	// Ajaxs
	$scope.submitNewProduct = function() {
		console.log($scope.order);
	}
	// $http({
	// method  : 'POST',
	// url     : 'http://gazuros-app.cfapps.io/orders/create',
	// data    : $scope.order,
	// headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
	// })
	// .success(function(data) {
	// console.log(data);

	// if (!data.success) {
	// // if not successful, bind errors to error variables
	// console.log('Error posting new product');
	// } else {
	// // if successful, bind success message to message
	// console.log('Success posting new product: ', data.message);
	// }
	// });
	// };

});

// Services
app.service('dataService', function($http) {
	this.getAllProjects = function() {
		return $http.get("http://" + host + "/products/getProducts").then(function(response) {
			return response;
		});
	}
	
	this.getBackOrders = function() {
		return $http.get("http://" + host + "/orders/getOrdersByStatus?status=BACK_ORDER").then(function(response) {
			return response;
		});
	}
	
	this.getKitNames = function() {
		return $http.get("http://" + host + "/inventory/getKitNames").then(function(response) {
			return response;
		});
	}
	
	this.updateBackOrder = function(i_productId, i_status) {
		var status = i_status || 'IN_STOCK';
		var link = "http://" + host + "/orders/update/" + i_productId + "?status=" + status; 
		
		return $http.put(link, {}).then(
		function(response) {
			// Success
			console.log("DATASERVICE: Updated product %s successfully", i_productId);
			return response;
		}, 
		function(response) {
			// Failure
			console.log("DATASERVICE: Failed to update product %s", i_productId);
			return response.status;
		});
	}

	this.removeKitsFromStock = function(i_kitName, i_numberOfKitsToRemove) {
		if (!!kitName) {
			var link = "http://" + host + "/inventory/removeNumKitsFromInventory?kitName=" + i_kitName + "&numKitsToRemove=" + i_numberOfKitsToRemove;
			
			return $http.put(link, {}).then(
			function(response) {
				// Success
				console.log("DATASERVICE: Removed %d %s kits successfully", i_numberOfKitsToRemove, i_kitName);
				return response;
			}, 
			function(response) {
				// Failure
				console.log("DATASERVICE: Failed to remove %s kits", i_kitName);
				return response.status;
			});
		}
	}
	
	this.removeBoxesFromStock = function(i_numberOfBoxesToRemove) {
		if (isInteger(i_numberOfBoxesToRemove)) {
			var link = "http://" + host + "/inventory/removeNumBoxes?numBoxesToRemove=" + i_numberOfBoxesToRemove;	
			
			return $http.put(link, {}).then(
			function(response) {
				// Success
				console.log("DATASERVICE: Removed %d boxes successfully", i_numberOfBoxesToRemove);
				return response;
			}, 
			function(response) {
				// Failure
				console.log("DATASERVICE: Failed to remove boxes");
				return response.status;
			});
		}
	}
});

app.service('projectsService', function($http, dataService) {
	this.mappings = {};
	var self = this;
	
	this.init = function() { // Get all projects and map id to number for easy reference
		dataService.getAllProjects().then(function(response) {
			if (response && response.data && response.data.length > 0) {
				var projects = response.data;
				
				projects.forEach(function(item) {
					self.mappings[item.id] = item.name;
				});
			}
		});
	}();
	
	this.getNameForId = function(i_productId) {
		if (self.mappings(i_productId)) {
			return self.mappings[i_productId];
		}
	}
}


// Silly polyfill
function isInteger(value) {
  return typeof value === "number" && 
    isFinite(value) && 
    Math.floor(value) === value;
};

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
