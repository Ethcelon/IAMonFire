import json


with open('transactions.json') as transaction_data:
    tdata = json.load(transaction_data)


for result in tdata['Data']['Transaction']:
    amount = result['Amount']['Amount']
    balance = result['Balance']['Amount']['Amount']
    print ("Transaction amount: " + (amount))
    print ("Balance after transaction: " + (balance))


with open('balance.json') as balance_data:
    bdata = json.load(balance_data)


for result in bdata['Data']['Balance']:
    balance = result['Amount']['Amount']
    print ("Account balance: " + (balance))
    








