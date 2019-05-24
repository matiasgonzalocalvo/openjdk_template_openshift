def aws_config(credential_id) {
    /*
        Funcion recibe por parametro el credential_id y setea el key_id y access_key como variable de entorno y configura el aws cli.
    */
    if ( "${AWS_REGION}" == "null" )
    {
        echo "seteo region a mano"
        env.AWS_REGION="us-east-1"
        AWS_REGION="us-east-1"
    }
    else
    {
        echo "region existe y su valor es = ${env.AWS_REGION}"
    }
    withCredentials([[
    $class: "AmazonWebServicesCredentialsBinding",
    credentialsId: "${credential_id}",
    accessKeyVariable: "AWS_ACCESS_KEY_ID",
    secretKeyVariable: "AWS_SECRET_ACCESS_KEY"
    ]]) {
        env.AWS_ACCESS_KEY_ID="${AWS_ACCESS_KEY_ID}"
        env.AWS_SECRET_ACCESS_KEY="${AWS_SECRET_ACCESS_KEY}"
        sh "aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}"
        sh "aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}"
        sh "aws configure set region ${AWS_REGION}"
        sh "aws configure list --profile default"
    }
}
def credentials_to_variable(env_variable, env_credentials)
{
  withCredentials([string(credentialsId: "${env_credentials}" , variable: "variable")]) 
  {
    echo "env_variable = ${env_variable} | variable = ${variable} | env_credentials = ${env_credentials}"
    evaluate "env.${env_variable}=\"${variable}\""
  }
  //sh "export | base64"
}
def send_slack(def estado=null,def emoji="ghost",def channel="#jenkins",def text="Job $JOB_NAME Build number $BUILD_NUMBER for branch $BRANCH_NAME ${RUN_DISPLAY_URL} |",def slackurl="https://hooks.slack.com/services/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") {
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}" 
}
def send_theeye(webhook,json) {
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
    /*
        Funcion que ejecuta maven verify recibe el archivo settings.xml de fomar opcional si no se pasa se ejecuta sin el parametro -s.
    */
    if ( settings == "null" )
    {
        sh "mvn verify -DskipTests -X"
    }
    else
    {
        sh "mvn verify -DskipTests -s ${settings} -X"
    }
}
def maven_sonar(def settings="null", def sonar_url="null", def sonar_login="null", def sonar_projectname="null")
{
    /*
        Funcion que ejecuta maven sonar. recibe como parametro opcionales settings sonar_url sonar_login y sonar_projectname
    */
    if( sonar_projectname != "null" )
    {
        echo "seteando sonar_projectname = ${sonar_projectname}"
        sonar="-Dsonar.projectName=${sonar_projectname}"
    }
    if( sonar_login != "null" )
    {
        echo "seteando sonar_login = ${sonar_login}"
        sonar="${sonar} -Dsonar.login=${sonar_login}"
    }
    if( sonar_url != "null" )
    {
        echo "seteando sonar_url = ${sonar_url}"
        sonar="${sonar} -Dsonar.host.url=${sonar_url}"
    }
    if( settings == "null" )
    {
        sh """
            mvn sonar:sonar -DskipTests -X ${sonar}
        """
    }
    else 
    {
        sh """
            mvn sonar:sonar -DskipTests -s ${settings} -X ${sonar}
        """
    }
}
def maven_deploy(def settings="null")
{
    if ( settings == "null" )
    {
        sh "mvn deploy -DskipTests -X"
    }
    else
    {
        sh "mvn deploy -DskipTests -s ${settings} -X"
    }
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
        docker -H "${url_docker_tcp}" push  ${url_repo}/${name}:${tag} 
    """
}
def jenkins_docker_build(def url_repo="null", def name="null",def tag="null",def url_docker_tcp="null")
{
    sh "echo ejecutando docker usando plugin de jenkins"
    app = docker.build("${url_repo}/${name}:${tag}")
}
def docker_tag(def url_repo="null",def name="null",def tag="null",def tag2="null",def url_docker_tcp="null")
{
    sh """ 
        docker -H "${url_docker_tcp}" tag  ${url_repo}/${name}:${tag} ${url_repo}/${name}:${tag2}
    """
}
def docker_pull(def url_repo="null",def name="null",def tag="null",def url_docker_tcp="null")
{
    sh """ 
        docker -H "${url_docker_tcp}" pull  ${url_repo}/${name}:${tag}
    """
}
def docker_login()
{
    sh "aws ecr get-login | sed 's/-e none//g' >> docker_login && bash docker_login"
}
def test_npm()
{
  sh "ls -atlrhR /mnt "
  sh """
    if ! [ -d "/mnt/efs/cache/" ] ; then
      mkdir -p /mnt/efs/cache/
    fi
    if [ -d "./libs/circuits-engine" ] ; then
      echo "existe ./libs/circuits-engine"
      cd ./libs/circuits-engine && $WORKSPACE/scripts/cache.sh && npm install && npm test && npm unlink && npm link
    fi
    if [ -d "./libs/circuits-engine-api-helper" ] ; then
      echo "existe ./libs/circuits-engine-api-helper"
      cd ./libs/circuits-engine-api-helper && $WORKSPACE/scripts/cache.sh && npm install && npm test && npm unlink && npm link
    fi
    if [ -f "$WORKSPACE/scripts/cache.sh" ] ; then
      find . -maxdepth 3 -type d \\( ! -name . \\) -exec bash -c "cd \'{}\' && pwd && if [ -f cloudformation.yaml ]; then $WORKSPACE/scripts/cache.sh && npm install && npm run test ; fi" \\;
    else
      find . -maxdepth 3 -type d \\( ! -name . \\) -exec bash -c "cd \'{}\' && pwd && if [ -f cloudformation.yaml ]; then npm install && npm run test ; fi" \\;
    fi
  """
  junit 'junit/**/*.xml'
  sh "export | base64 "
}
def sonar_js(sonar_projectKey, sonar_exclusions, sonar_javascript_lcov_reportPaths)
{
  sonar_login="694e463e93ba0a27427fb8a46a266abc42c0f542"
  def scannerHome = tool 'SonarQube Scanner';
    withSonarQubeEnv('Sonarqube') {
      sh "export"
      sh "${scannerHome}/bin/sonar-scanner \
        -Dsonar.projectKey=${sonar_projectKey} \
        -Dsonar.projectVersion=${BUILD_NUMBER} \
        -Dsonar.projectBaseDir=${WORKSPACE} \
        -Dsonar.sources=. \
        -Dsonar.language=js \
        -Dsonar.exclusions=${sonar_exclusions} \
        -Dsonar.javascript.lcov.reportPaths=${sonar_javascript_lcov_reportPaths} \
        -Dsonar.login=${sonar_login} -X"
    }
}
def wait_sonar()
{
    timeout(time: 1, unit: 'HOURS')
    {
      sh "export"
      // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
      // true = set pipeline to UNSTABLE, false = don't
      // Requires SonarQube Scanner for Jenkins 2.7+
      def qg = waitForQualityGate()
      //waitForQualityGate abortPipeline: true
      if (qg.status != 'OK') 
      {
        error "Pipeline aborted due to quality gate failure: ${qg.status}"
      }
    }
}
def build_comafi_digital()
{
  echo 'Building..'
  sh 'env'
  sh 'chmod 755 scripts/build.sh'
  sh 'cd scripts && ./build.sh'
}
def deploy_comafi_digital()
{
  echo 'Deploying....'
  sh 'chmod 755 scripts/deploy.sh'
  sh 'cd scripts && bash -x ./deploy.sh'
}
def git_tag(def credentials="devops-bitbucket")
{
  //sh 'echo version=' + env.BRANCH_NAME + '.' + env.BUILD_NUMBER + ' > version.info'
  sh 'echo "version=${BRANCH_NAME}.${BUILD_NUMBER}" > version.info'
  //env.git_url = "${GIT_URL}".drop(8)
  //GIT_URL.split(/\/{2}/) 
  withCredentials([usernamePassword(credentialsId: credentials,
    passwordVariable: 'GIT_PASS', usernameVariable: 'GIT_USER')]) {
    sh """
      git tag `echo $BRANCH_NAME | cut -d "/" -f2`.$BUILD_NUMBER
      git push https://devops-comafi:$GIT_PASS@`echo ${GIT_URL} | sed 's"http://""g'|sed 's"https://""g'` --tags
      echo "test export git_tag"
      export
    """
  }
}

return this
