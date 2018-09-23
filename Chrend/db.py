from tinydb import TinyDB, Query

db = TinyDB('db.json')

def bootstrap_db():
    global db
    users_table = db.table('users')
    User = Query()
    found = users_table.search(User.name == 'om2')
    if found:
        print('User', found)
    else:
        new = users_table.insert({'name': 'om2', 'password': 'testaccount'})
        print('Inserted User', new)

def save_nonce(nonce):
    global db
    state_table = db.table('state')
    new = state_table.insert({'nonce': 'om2'})
    print('Inserted nonce {} in state_table with id {}'.format(nonce, new))

def validate_nonce(nonce):
    global db
    state_table = db.table('state')
    Nonce = Query()
    return db.contains(Nonce.state == nonce)

def write_account_token_to_db(token):
    global db
    account_access_tokens = db.table('account_access_tokens')
    TokenQ = Query()
    print("Writing token {} for user {}.", token, 'om2')
    account_access_tokens.upsert({'user': 'om2', 'account_access_token': token}, TokenQ.user == 'om2')

def read_account_token_from_db():
    global db
    account_access_tokens = db.table('account_access_tokens')
    TokenQ = Query()
    token_obj = account_access_tokens.get(TokenQ.user == 'om2')
    print("Found token {} for user {}.", token_obj, 'om2')
    return token_obj['account_access_token']