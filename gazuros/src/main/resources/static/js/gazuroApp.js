var app = angular.module('gazuroApp', []);
var host = "127.0.0.1:8080";
//var host = "gazuros-app.cfapps.io";

// Controllers
app.controller('projectController', function($scope, $window, $location, dataService) {
	$scope.projects = [{"id":32,"name":"pot","price":0.0,"provider":"jiffy","description":"pots"},{"id":42,"name":"soil pellet","price":0.0,"provider":"jiffy","description":"soil pellet"},{"id":52,"name":"plant marker","price":0.0,"provider":"mastertag","description":"plant markers"},{"id":62,"name":"polybag for soil","price":0.0,"provider":"amazon","description":"polybag for soil"},{"id":72,"name":"master carton","price":0.0,"provider":"office depot","description":"master carton - can hold up to 24 units"},{"id":82,"name":"bonsai instructions","price":0.0,"provider":"china","description":"bonsai instructions"},{"id":92,"name":"bonsai packaging box","price":0.0,"provider":"china","description":"bonsai packaging boxe"},{"id":102,"name":"jacaranda seed packet","price":0.0,"provider":"page seed","description":"jacranda packet"},{"id":112,"name":"Delonix seed packet","price":0.0,"provider":"page seed","description":"Delonix seed packet"},{"id":122,"name":"Picea seed packet","price":0.0,"provider":"page seed","description":"Picea seed packet"},{"id":132,"name":"Pinus seed packet","price":0.0,"provider":"page seed","description":"Pinus seed packet"},{"id":142,"name":"herb instructions","price":0.0,"provider":"china","description":"herbs instructions"},{"id":152,"name":"herb packaging box","price":0.0,"provider":"china","description":"herb packaging box"},{"id":162,"name":"basil","price":0.0,"provider":"page seed","description":"basil seed packet"},{"id":172,"name":"sage","price":0.0,"provider":"page seed","description":"sage seed packet"},{"id":182,"name":"parsley","price":0.0,"provider":"page seed","description":"parsley seed packet"},{"id":192,"name":"thyme","price":0.0,"provider":"page seed","description":"thyme seed packet"},{"id":202,"name":"cilantro","price":0.0,"provider":"page seed","description":"cilantro seed packet"},{"id":212,"name":"Crazy garden instructions","price":0.0,"provider":"china","description":"Crazy garden instructions"},{"id":222,"name":"Crazy garden packaging box","price":0.0,"provider":"china","description":"Crazy garden packaging box"},{"id":232,"name":"Purple carrot","price":0.0,"provider":"page seed","description":"Purple carrot seed packet"},{"id":242,"name":"Black corn","price":0.0,"provider":"page seed","description":"Black corn seed packet"},{"id":252,"name":"Lemon cucumber","price":0.0,"provider":"page seed","description":"Lemon cucumber seed packet"},{"id":262,"name":"5 color swiss chard","price":0.0,"provider":"page seed","description":"5 Color swiss chard seed packet"},{"id":272,"name":"Romanesco Brocolli","price":0.0,"provider":"page seed","description":"Romanesco Brocolli seed packet"}]

	dataService.getAllProjects().then(function(response) {
		if (response && response.data) {
			$scope.projects = response.data;	
		}
	});

});

app.controller('inventoryController', function($scope, $window, $location, dataService) {
	$scope.currentInventory = [{name: "master carton", amount: 8000}];
	
	dataService.getCurrentInventory().then(response) {
		if (response && response.data) {
			$scope.currentInventory = formatInventoryResponse(response.data);	
		}
	}
	
	function formatInventoryResponse(i_responseObj) {
		var keys = Object.keys(i_responseObj);
		var output = [];
		
		keys.forEach(function(key) {
			var tempObj = {
				name : key,
				amount : i_responseObj[key]
			};
			
			output.push(tempObj);
		});
		
		return output;
	}

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

app.controller('menuController', function($scope, $http, $window, $location, dataService, projectsService) {
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

	$scope.order = {shippingPrice : 0.0};
	$scope.mappings = {};
	$scope.products = {};
	
	projectsService.init(function(mappings) {
		$scope.mappings = mappings;
	});



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
			
			if ($scope.backorders && $scope.backorders.length > 0) {
				$scope.backorders.forEach(function(order) {
				order.name = getNameForId(order.productId);
				});
			}
		}
	});
	
	dataService.getAllProjects().then(function(response) {
		if (response && response.data) {
			$scope.products = response.data;	
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
			$scope.itemToConfirm = i_order.name;
			$scope.showPrompt = true;
		}
	}
	
	$scope.verifyBeforeOrder = function() {
		$scope.confirmShip = true;
	}
	
	$scope.updateBackOrder = function(i_order) {
		dataService.updateBackOrder(i_order.id).then(function(response) {
			console.log(response);
			$scope.hidePrompt();
		});
	}
	
	// Stupid naming, remove boxes from back order
	$scope.placeOrder = function(i_order) {
		if (!!i_order) {
			$scope.selectedKit = i_order;
			$scope.currentOrder = { numberOfBoxes : 0, unitsPerBox : 0};
			$scope.showOrderPrompt = true;
		}
	}
	// Stupid naming, remove boxes from back order
	$scope.confirmOrder = function(i_selectedKit) {
		console.log("CO:");
		console.log($scope.currentOrder);
		var totalKitsToRemove = ($scope.currentOrder.numberOfBoxes * $scope.currentOrder.unitsPerBox);
		var boxesToRemove = $scope.currentOrder.numberOfBoxes;
		
		if (isInteger(totalKitsToRemove)) {
			// Remove kits and boxes
			dataService.removeKitsFromStock(i_selectedKit, totalKitsToRemove).then(function(response) {
				console.log(response);
			});
			
			dataService.removeBoxesFromStock(boxesToRemove).then(function(response) {
				console.log(response);
			});
		}
	}
	
	// Create new product
	$scope.submitNewProduct = function() {
		dataService.createNewProduct($scope.order).then(function(response) {
			$scope.order = {shippingPrice : 0.0}; // Reset order object
	});
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
		var url = "inventory.html";
		$window.location.href = url;
	}

	$scope.goToProductPage = function() {
		var url = "projects.html";
		$window.location.href = url;
	}
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
	
	
	this.getCurrentInventory = function() {
		return $http.get("http://" + host + "/inventory/getCurrentInventory").then(function(response) {
			return response;
		});
	}}
	
	
	
	this.updateBackOrder = function(i_orderId, i_status) {
		var link = "http://" + host + "/orders/update/" + i_orderId + "?status=IN_STOCK";

		return $http.put(link, {}).then(
		function(response) {
			// Success
			console.log("DATASERVICE: Updated order %s successfully", i_orderId);
			return response;
		}, 
		function(response) {
			// Failure
			console.log("DATASERVICE: Failed to update order %s", i_orderId);
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
	
	this.createNewProduct = function(i_productObj) {
		if (i_productObj && i_productObj != null) {
			var link = "http://" + host + "/orders/create";	
			console.log("Adding...");
			console.log(i_productObj);
			
			return $http.post(link, i_productObj).then(
			function(response) {
				// Success
				console.log("DATASERVICE: Product created successfully");
				return response;
			}, 
			function(response) {
				// Failure
				console.log("DATASERVICE: Failed to create product");
				return response.status;
			});
		}
	}
});

app.service('projectsService', function($http, dataService) {
	var self = this;
	self.mappings = {};

	this.init = function(callback) { // Get all projects and map id to number for easy reference
		dataService.getAllProjects().then(function(response) {
			if (response && response.data && response.data.length > 0) {
				var projects = response.data;
				
				projects.forEach(function(item) {
					self.mappings[item.id] = item.name;
				});
			}

			if (callback) {
				callback(self.mappings);
			}
			else {
				return self.mappings;
			}
		});
	};
	
	this.getNameForId = function(i_productId) {
		if (self.mappings[i_productId]) {
			return self.mappings[i_productId];
		}
	}
});


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
