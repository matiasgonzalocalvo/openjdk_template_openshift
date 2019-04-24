def aws_config() {
    withCredentials([[
    $class: 'AmazonWebServicesCredentialsBinding',
    credentialsId: 'AWS_DESA',
    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
    ]]) {
        sh 'AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
        AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
        AWS_REGION=us-east-1'
        sh "aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}"
        sh "aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}"
        sh "aws configure set region ${AWS_REGION}"
        sh "aws configure list --profile default"
    }
}
def send_slack(def estado=null,def emoji="ghost",def channel="#jenkins",def text="Job $JOB_NAME Build number $BUILD_NUMBER for branch $BRANCH_NAME has",def slackurl="https://hooks.slack.com/services/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") {
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}" 
}

def test() {
    sh "echo test"
}
