from binance.client import Client  #https://github.com/sammchardy/python-binance
import random

client = Client('0fMbfnBaPbP2qevMNHvt5YHhdyZ15CcTHiOKgqwmDeyq41JtyTUsB8EfBixWurNa', 'mnuklEVS3ygcpUlXaWaGwBTXRrBol6ZMHXRnN2LdtEwpLJbmYHcH5LQpu7TnqPPs')

symbol = 'BNTBTC'
amount = 50
side = Client.SIDE_BUY  #BUY/SELL
first_order_percentage = 30.0  #first order percentage - market price
spread = 7  #how many orders
spread_step_size_percentage = 5.0 # percent from previous order (starting with market price)

#metadata
dryMode = True
side_str = 'selling' if side == Client.SIDE_SELL else 'buying'

symbol_info = client.get_symbol_ticker(symbol=symbol)
market_price = symbol_info['price']
print('current market price: ' + market_price)

print(side_str + ' ' + str(amount) + ' ' + symbol[0:3])

quantity_first_order = (first_order_percentage/100.0) * amount
quantity_spread = amount - quantity_first_order

print(side_str + ' ' + str(quantity_first_order) + ' ' + symbol[0:3] + ' at MARKET price')
print(side_str + ' ' + str(quantity_spread) + ' ' + symbol[0:3] + ' at SPREAD')

print(side_str + ' ' + str(quantity_first_order) + ' ' + symbol[0:3] + ' at MARKET price')

#1. Place Market BUY order
if not dryMode:
	first_order = client.create_order(
	    symbol=symbol,
	    type=Client.ORDER_TYPE_MARKET,
	    side=side,
	    quantity=quantity_first_order)
	print(first_order)

base = quantity_spread / (spread-1)
print('base: ' + str(base))
for i in range(1,spread):
	
	quantity_spread_i = base - (i * random.uniform(0.01, 0.03))
	quantity_spread_i_rounded = float("{0:.0f}".format(quantity_spread_i))  #FOR BNT precision has to be ZERO! (binance api thing) - raide precision for others

	price_i = float(market_price) 
	if side == Client.SIDE_SELL:
		price_i += i * (float(market_price) * (spread_step_size_percentage/100.0)) #sell above market price
	else:
		price_i -= i * (float(market_price) * (spread_step_size_percentage/100.0)) #buy below market price

	price_i_rounded = float("{0:.7f}".format(price_i))

	print(side_str + ' ' + str(quantity_spread_i_rounded) + ' ' + symbol[0:3] + ' at ' + str(price_i_rounded) + ' price')

	if not dryMode:
		order_i = client.create_order(
			symbol=symbol,
			quantity=quantity_spread_i_rounded,
			side=side,
			type=Client.ORDER_TYPE_LIMIT,
			price=price_i_rounded,
			timeInForce=Client.TIME_IN_FORCE_GTC)
		print(order_i)




