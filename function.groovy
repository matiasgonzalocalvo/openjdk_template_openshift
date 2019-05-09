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
def send_slack(def estado=null,def emoji="ghost",def channel="#jenkins",def text="Job $JOB_NAME Build number $BUILD_NUMBER for branch $BRANCH_NAME ${RUN_DISPLAY_URL} |",def slackurl="https://hooks.slack.com/services/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") {
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}" 
}
def update_aws_credentials(UPDATE_SERVICE_KEY_ID,UPDATE_SERVICE_SECRET_KEY)
{
    env.AWS_ACCESS_KEY_ID = "${UPDATE_SERVICE_KEY_ID}"
    env.AWS_SECRET_ACCESS_KEY = "${UPDATE_SERVICE_SECRET_KEY}"    
}
def maven_verify(def settings="null")
{
    sh "mvn verify -DskipTests -s ${settings} -X"
}
def maven_sonar(def settings="null", def sonar_url="null", def sonar_login="null", def sonar_projectname="null")
{
    sh "echo \"sonar_projectname == ${sonar_projectname}\""
    if( sonar_projectname == "null" )
    {
        sh """
            echo "sonar_projectname == ${sonar_projectname}"
            mvn sonar:sonar -DskipTests -s ${settings} -X -Dsonar.host.url=${sonar_url} -Dsonar.login=${sonar_login}
        """
    }
    else 
    {
        sh """
            echo sonar_projectame == ${sonar_projectname}
            mvn sonar:sonar -DskipTests -s ${settings} -X -Dsonar.host.url=${sonar_url} -Dsonar.login=${sonar_login} -Dsonar.projectName=${sonar_projectname}
        """
    }
}
def maven_deploy(def settings="null")
{
    sh "mvn deploy -DskipTests -s ${settings} -X"
}
def docker_build(def url_repo="null", def name="null",def tag="null",def url_docker_tcp="null")
{
    sh """
        docker -H "${url_docker_tcp}" build -t ${url_repo}/${name}:${tag} .
    """
}
def docker_push(def url_repo="null",def name="null",def tag="null",def url_docker_tcp="null")
{
    sh """ 
        $(aws ecr login)
        docker -H "${url_docker_tcp}" push -t ${url_repo}/${name}:${tag} .
    """
}
def jenkins_docker_build(def url_repo="null", def name="null",def tag="null",def url_docker_tcp="null")
{
    //sh """
    //    docker -H "${url_docker_tcp}" build -t ${url_repo}/${name}:${tag} .
    //"""
    sh "echo ejecutando docker usando plugin de jenkins"
    app = docker.build("${url_repo}/${name}:${tag}")
}
return this
