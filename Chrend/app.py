from flask import Flask, jsonify, request, send_from_directory, redirect, abort
from flask_cors import CORS
import json

from db import bootstrap_db, save_nonce, validate_nonce

from helpers import create_account_request_and_give_me_hybrid_request
from helpers import exchange_code_for_token
from db import write_account_token_to_db, get_accounts, save_account

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
def get_accounts():

    token = read_account_token_from_db()
    accounts = get_accounts_help(token)

    list_accounts = accounts["data"]["Account"]
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

    print(request.form.get('selectedAccount'))
    print(request.form.get('bidAmount'))

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

    write_account_token_to_db(access_token)

    return redirect("localhost:3000/auction", code=302)

@app.route('/payment', methods=['POST'])
def capture_payment():
    if not request.json:
        abort(400)

    amount_json = json.dumps(request.get_json(request.json))
    amount = json.loads(amount_json)

    json_data = (str(amount['Data']['Initiation']['InstructedAmount']['Amount']))

    return (json_data)


if __name__ == "__main__":
    context = ('keys/cert.pem', 'keys/key.pem')
    app.run(host='0.0.0.0', port=80, ssl_context=context, threaded=True, debug=True)