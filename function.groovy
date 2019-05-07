def aws_config(credential_id, def AWS_ACCESS_KEY_ID="AWS_ACCESS_KEY_ID", def AWS_SECRET_ACCESS_KEY="AWS_SECRET_ACCESS_KEY") {
    echo "${credential_id} ${AWS_ACCESS_KEY_ID} ${AWS_SECRET_ACCESS_KEY}"
    withCredentials([[
    $class: "AmazonWebServicesCredentialsBinding",
    credentialsId: "${credential_id}",
    accessKeyVariable: "${AWS_ACCESS_KEY_ID}",
    secretKeyVariable: "${AWS_SECRET_ACCESS_KEY}"
    ]]) {
        evaluate "env.${AWS_ACCESS_KEY_ID}=${AWS_ACCESS_KEY_ID}"
        evaluate "env.${AWS_SECRET_ACCESS_KEY}=${AWS_SECRET_ACCESS_KEY}"
    }
}
def send_slack(def estado=null,def emoji="ghost",def channel="#jenkins",def text="Job $JOB_NAME Build number $BUILD_NUMBER for branch $BRANCH_NAME has",def slackurl="https://hooks.slack.com/services/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") {
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}" 
}

def test() {
    sh "echo test"
}
return this
