import requests
import json
import time
import uuid

KEY_PATH_NAME = "keys/f53d8e94-3e45-49eb-b3ec-0835f61f9b80"
CLIENT_CERT = ( KEY_PATH_NAME + '.pem', KEY_PATH_NAME + '.key')
CLIENT_ID = "5af1bffd-5a30-4aa8-b09c-7e140401788b"
AUD = "https://matls.as.aspsp.ob.forgerock.financial/oauth2/openbanking"
CLIENT_REDIRECT_URI = "https://www.google.com"  #https://localhost:80 

def make_request(method, url, payload=None, headers=None, cert=CLIENT_CERT, verify=True):
    r = None
    if method == "get":
        r = requests.get(url, verify=verify, headers=headers, cert=cert)
    if method == "post":
        r = requests.post(url, data=payload, headers=headers, cert=cert, verify=verify)
    if method == "delete":
        r = requests.delete(url, data=payload, headers=headers, cert=cert, verify=verify)

    # TODO: move response to class.
    # set the response regardless or errors
    try:
        
        response = r.json()
    except ValueError:
        response = {"error": "Json decoding error", "raw": r.text}

    status_code = r.status_code
    return response, status_code

def gen_client_credential_jwt():

    url = "https://jwkms.ob.forgerock.financial:443/api/crypto/signClaims"

    # Set 5 minute expiry
    exp = time.time() +60*5

    payload = "{\n  \"sub\": \"" + CLIENT_ID + "\",\n  \"exp\": " + str(exp) +",\n  \"aud\": \"" + AUD + "\"\n}\n"

    headers = {
        'Content-Type': "application/json",
        'issuerId': CLIENT_ID,
        'Cache-Control': "no-cache"
        }

    response, status_code = make_request('post', url, payload=payload, headers=headers)
    print("Got client credential", response)#, response

    # Expected decode error
    response = response['raw']
    return response

def get_access_token():

    client_assertion = gen_client_credential_jwt()

    url = "https://matls.as.aspsp.ob.forgerock.financial/oauth2/realms/root/realms/openbanking/access_token"

    payload = "grant_type=client_credentials&scope=openid%20accounts&client_assertion_type=urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer&client_assertion=" + client_assertion

    headers = {
        'Content-Type': "application/x-www-form-urlencoded",
        'Cache-Control': "no-cache"
        }

    response, status_code = make_request('post', url, payload=payload, headers=headers)

    print("Got access token")#, access_token

    return response["access_token"]

def create_account_request():

    access_token = get_access_token()

    url = "https://rs.aspsp.ob.forgerock.financial:443/open-banking/v2.0/account-requests"

    payload = "{\n  \"Data\": {\n    \"Permissions\": [\n      \"ReadAccountsDetail\",\n      \"ReadBalances\",\n      \"ReadBeneficiariesDetail\",\n      \"ReadDirectDebits\",\n      \"ReadProducts\",\n      \"ReadStandingOrdersDetail\",\n      \"ReadTransactionsCredits\",\n      \"ReadTransactionsDebits\",\n      \"ReadTransactionsDetail\",\n      \"ReadOffers\",\n      \"ReadPAN\",\n      \"ReadParty\",\n      \"ReadPartyPSU\",\n      \"ReadScheduledPaymentsDetail\",\n      \"ReadStatementsDetail\"\n    ],\n    \"TransactionFromDateTime\": \"2017-05-03T00:00:00+00:00\",\n    \"TransactionToDateTime\": \"2018-12-03T00:00:00+00:00\"\n  },\n  \"Risk\": {}\n}"

    headers = {
        'Authorization': "Bearer " + access_token,
        'Content-Type': "application/json",
        'x-idempotency-key': "FRESCO.21302.GFX.20",
        'x-fapi-financial-id': "0015800001041REAAY",
        'x-fapi-customer-last-logged-time': "Sun, 10 Sep 2017 19:43:31 UTC",
        'x-fapi-customer-ip-address': "104.25.212.99",
        'x-fapi-interaction-id': "93bac548-d2de-4546-b106-880a5018460d",
        'Accept': "application/json",
        'Cache-Control': "no-cache"
        }

    response, status_code = make_request('post', url, payload=payload, headers=headers)

    print(response, status_code)

def gen_account_request_parameter(state):
    import requests

    url = "https://jwkms.ob.forgerock.financial:443/api/crypto/signClaims"

    # Set 5 minute expiry
    exp = time.time() +60*5

    payload = "{\n  \"aud\": \""+AUD+"\",\n  \"scope\": \"openid accounts\",\n  \"iss\": \""+CLIENT_ID+"\",\n  \"claims\": {\n    \"id_token\": {\n      \"acr\": {\n        \"value\": \"urn:openbanking:psd2:sca\",\n        \"essential\": true\n      },\n      \"openbanking_intent_id\": {\n        \"value\": \"A441af61d-84e8-457f-908e-69036fdc76f1\",\n        \"essential\": true\n      }\n    },\n    \"userinfo\": {\n      \"openbanking_intent_id\": {\n        \"value\": \"A441af61d-84e8-457f-908e-69036fdc76f1\",\n        \"essential\": true\n      }\n    }\n  },\n  \"response_type\": \"code id_token\",\n  \"redirect_uri\": \"https://www.google.com\",\n  \"state\": \""+state+"\",\n  \"exp\": "+str(exp)+",\n  \"nonce\": \""+state+"\",\n  \"client_id\": \""+CLIENT_ID+"\"\n}"

    headers = {
        'Content-Type': "application/json",
        'jwkUri': "https://as.aspsp.ob.forgerock.financial/oauth2/realms/root/realms/openbanking/connect/jwk_uri",
        'issuerId': CLIENT_ID,
        'Cache-Control': "no-cache"
        }

    response, status_code = make_request('post', url, payload=payload, headers=headers)

    # Expected decode error
    response = response['raw']
    return response

def generate_hybrid_url_with_account_rquest_parameter():

    nonce = str(uuid.uuid4())
    request_parameter = gen_account_request_parameter(nonce)

    url = "{AS_authorization_endpoint}?response_type=code id_token&client_id={CLIENT_ID}&state={nonce}&nonce={nonce}&scope=openid payments accounts&redirect_uri={CLIENT_REDIRECT_URI}&request={request_parameter}".format(
        AS_authorization_endpoint = 'https://matls.as.aspsp.ob.forgerock.financial/oauth2/realms/root/realms/openbanking/authorize',
        CLIENT_ID=CLIENT_ID,
        state=nonce,
        nonce=nonce,
        CLIENT_REDIRECT_URI=CLIENT_REDIRECT_URI,
        request_parameter=request_parameter
    )

    return url, nonce

def create_account_request_and_give_me_hybrid_request():
    create_account_request()
    return generate_hybrid_url_with_account_rquest_parameter()

def exchange_code_for_token(code):

    url = "https://matls.as.aspsp.ob.forgerock.financial/oauth2/realms/root/realms/openbanking/access_token"

    client_assertion = gen_client_credential_jwt()

    payload = "grant_type=authorization_code&code="+code+"&redirect_uri=https%3A%2F%2Flocalhost:80%2Foauth2%2Fredirect%2Fredirect.html&client_assertion_type=urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer&client_assertion=" + client_assertion

    headers = {
        'Content-Type': "application/x-www-form-urlencoded",
        'Cache-Control': "no-cache"
        }

    response, status_code = make_request('post', url, payload=payload, headers=headers)

    return response["access_token"]

#gen_stuff_and_print_req()