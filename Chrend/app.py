from flask import Flask, jsonify, request, send_from_directory, redirect, abort
from flask_cors import CORS
import json

from db import bootstrap_db, save_nonce, validate_nonce, save_account

from helpers import create_account_request_and_give_me_hybrid_request
from helpers import exchange_code_for_token, get_accounts_help, get_balance, make_payment
from db import write_account_token_to_db, get_accounts, save_account, read_account_token_from_db

bootstrap_db()

app = Flask(__name__)
CORS(app)

@app.route('/')
def hello_world():
    return "{}"

@app.route('/login', methods=['POST'])
def login():
    if request.method == 'POST':
        url, nonce = create_account_request_and_give_me_hybrid_request()
        save_nonce(nonce)

        return redirect(url, code=302)
    else:
        return "{}"

@app.route('/oauth2/redirect/<path:path>')
def serve_html(path):
    return send_from_directory('html', path)

@app.route('/user/accounts')
def get_accounts_view():

    token = read_account_token_from_db()
    accounts = get_accounts_help(token)
    print(accounts)
    list_accounts = accounts["Data"]["Account"]
    nicks = []

    for account in list_accounts:
        nicks.append(account["Nickname"])

    save_account(accounts)
    print(accounts)
    return jsonify(nicks), 200

@app.route('/auction/makebid', methods=['POST'])
def parse_bid():
    if request.method == 'POST':
        print(request.form)

    accesstoken = read_account_token_from_db()
    accounts = get_accounts("om2")
    print(accounts)
    acc2 = {}

    for acc in accounts["accounts"]["Data"]["Account"]:
        acc2[acc["Nickname"]] = acc["AccountId"]

    accountid = acc2[request.form.get('selectedAccount')]
    bidamount = request.form.get('bidAmount')

    print(request.form.get('selectedAccount'), accountid)
    print(request.form.get('bidAmount'), bidamount)

    balance = get_balance(accountid, accesstoken)
    print(balance)
    if float(balance['Data']['Balance'][0]['Amount']['Amount']) < float(bidamount):
        print ("You don't have the money dude!")
        return jsonify({"bidStatus" : "You don't have the money dude!"}), 200
    else:
        try:
            from helpers import make_payment, create_payment_request
            print("calling make payment")
            req = create_payment_request(bidamount)
            print(req)
            return jsonify({"Redirect:" : req, "code" : 302}), 200
        except Exception as e:
            print(e)
        return jsonify({"bidStatus" : "Success"}), 200



@app.route('/oauth2/capture')
def capture_oauth2():

    id_token=request.args.get('id_token')
    code=request.args.get('code')
    state=request.args.get('state')

    if id_token is None or id_token is "undefined":
        return jsonify({"status" : "missing id_token"})
    if code is None or code == "undefined":
        return jsonify({"status" : "missing code"})
    if state is None or state == "undefined":
        return jsonify({"status" : "missing state"})

    if not validate_nonce:
        return jsonify({"status" : "Fishy Fishy! Bad nonce!"})

    access_token = exchange_code_for_token(code)

    return redirect("http://localhost:3000/auction", code=302)

@app.route('/payment', methods=['POST'])
def capture_payment():
    if not request.json:
        abort(400)

    amount_json = json.dumps(request.get_json(request.json))
    amount = json.loads(amount_json)

    json_data = (str(amount['Data']['Initiation']['InstructedAmount']['Amount']))

    return (json_data)


@app.route('/balance', methods=['POST'])
def accounts():
    if not request.json:
        abort(400)

    balance_json = json.dumps(request.get_json(request.json))
    bjson = json.loads(balance_json)

    json_data=[]

    for key in bjson:
        for sub_key in bjson[key]:
            if 'Balance' in sub_key:
                json_data = (str(bjson['Data']['Balance'][0]['Amount']))
            elif 'Transaction' in sub_key:
                for i in bjson['Data']['Transaction']:
                  json_data.append(i['Amount'])

    return (str(json_data))

if __name__ == "__main__":
    context = ('keys/cert.pem', 'keys/key.pem')
    app.run(host='0.0.0.0', port=80, ssl_context=context, threaded=True, debug=True)