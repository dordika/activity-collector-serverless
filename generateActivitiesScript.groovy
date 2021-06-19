//Resource
List<String> appSource = ['GOMP', "e-Learing", "webSite" ]
List<String> appVersions = ['1.0', '2.0']
List<String> instances = ['Cloud', 'OnPremise']
List<String> activities = ['exception', 'login', 'logout', 'help',
                        'visitPage1', 'visitPage2', 'visitPage3', 'permissionDenied']
List<String> appSessionIdentifiers = ['000', '111', '222', '333', '444', '555']



String loginLogoutEventValue = "{'username':'Joe'}"
Map<String, String> activityValues = ['exception': "{'message': 'internal server error', 'errorCode': '505' }",
                                  'login': loginLogoutEventValue,
                                  'logout': loginLogoutEventValue]

String defaultActivityValue = "{'pageName':'namex'}"

def r = new Random()


for (int i=0; i<50; i++) {
    String activity = activities[r.nextInt(activities.size())]
    String activityValue = activityValues.containsKey(activity) ? activityValues.get(activity) : defaultActivityValue
    String body =  "{\"appSource\": \"${appSource[r.nextInt(appSource.size())]}\", " +
                   "{\"appVersion\": \"${appVersions[r.nextInt(appVersions.size())]}\", " +
                   "\"instance\":  \"${instances[r.nextInt(instances.size())]}\",\"activity\": \"${activity}\", " +
                   "\"sessionIdentifier\": \"${appSessionIdentifiers[r.nextInt(appSessionIdentifiers.size())]}\", " +
                   "\"activityValue\": \"${activityValue}\"}"
    sendRequest(body)
}

private void sendRequest(String body) {
    String urlLambda = 'https://userId.execute-api.us-east-1.amazonaws.com/dev/activities'
    def post = new URL(urlLambda).openConnection();
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write(body.getBytes("UTF-8"));
    def postRC = post.getResponseCode();
    
    
    println(postRC);
}

