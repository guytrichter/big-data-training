from binance.client import Client  #https://github.com/sammchardy/python-binance
from decimal import Decimal


client = Client('0fMbfnBaPbP2qevMNHvt5YHhdyZ15CcTHiOKgqwmDeyq41JtyTUsB8EfBixWurNa', 'mnuklEVS3ygcpUlXaWaGwBTXRrBol6ZMHXRnN2LdtEwpLJbmYHcH5LQpu7TnqPPs')
dryMode = True

account = client.get_account()
balances = account['balances']

def get_num_symbols( tickers_info ):
	i = 0
	for ticker_info in tickers_info:
			symbol = ticker_info['symbol']
			price = ticker_info['price']
			is_btc_symbol = symbol[-3:] == 'BTC'
			if (is_btc_symbol):
				i = i + 1
	return i

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
		
		free_balance = balance['free']
		tickers_info = client.get_all_tickers()
		quantity_in_btc = Decimal(free_balance) / get_num_symbols(tickers_info)
		print("selling " + str(free_balance) + " BTC equally at: " + str(quantity_in_btc))

		for ticker_info in tickers_info:
			symbol = ticker_info['symbol']
			price = ticker_info['price']
			is_btc_symbol = symbol[-3:] == 'BTC'
			if (is_btc_symbol):
				symbol_info = client.get_symbol_info(symbol=symbol)
				filters = symbol_info['filters']
				for symbol_filter in filters:
					if (symbol_filter['filterType'] == 'LOT_SIZE'):
						precision = get_precision(symbol_filter['minQty'])	
						quantity_to_buy_f = Decimal(float(quantity_in_btc) / float(price))
						quantity_to_buy = round(quantity_to_buy_f, precision)
						print('buying ' + str(quantity_to_buy) + ' ' + str(symbol) + ' at market price: ' + price)
						if not dryMode:
							try: 
								order = client.create_order(
						    		symbol=symbol,
						    		type=Client.ORDER_TYPE_MARKET,
						    		side = Client.SIDE_BUY,
					    			quantity=quantity_to_buy)
								print(order)
							except:
								print("Failed to buy: " + symbol)









