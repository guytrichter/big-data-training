from binance.client import Client  #https://github.com/sammchardy/python-binance
from decimal import Decimal

client = Client('0fMbfnBaPbP2qevMNHvt5YHhdyZ15CcTHiOKgqwmDeyq41JtyTUsB8EfBixWurNa', 'mnuklEVS3ygcpUlXaWaGwBTXRrBol6ZMHXRnN2LdtEwpLJbmYHcH5LQpu7TnqPPs')
dryMode = True

account = client.get_account()
balances = account['balances']

def get_precision(min_quantity):
	if (float(min_quantity) == 0.001):
		return 3
	if (float(min_quantity) == 0.01):
		return 2
	if (float(min_quantity) == 0.1):
		return 1
	if (float(min_quantity) == 1.0):
		return 0
	return -1

for balance in balances:
	ticker = balance['asset']
	if (ticker=='BTC'):
		continue

	free_balance = balance['free']
	hasFreeBalance = float(free_balance) > 0.0
	if hasFreeBalance:
		# print(str(ticker) + " has balance of: " + str(free_balance))
		symbol = ticker + 'BTC'
		symbol_ticker = client.get_symbol_ticker(symbol=symbol)
		symbol_info = client.get_symbol_info(symbol=symbol)
		filters = symbol_info['filters']
		for symbol_filter in filters:
			if (symbol_filter['filterType'] == 'LOT_SIZE'):
				precision = get_precision(symbol_filter['minQty'])
				quantity = round(Decimal(free_balance), precision)
				if (quantity < float(free_balance) or float(free_balance) < float(symbol_filter['minQty'])):
					print("Not enough funds to sell min order for: " + ticker)
					continue

				market_price = symbol_ticker['price']
				print('selling ' + str(quantity) + ' ' + ticker + ' at current market price: ' + market_price)
				if not dryMode:
					client.create_order(
					    symbol=symbol,
					    type=Client.ORDER_TYPE_MARKET,
					    side = Client.SIDE_SELL,
					    quantity=quantity)






