def aws_config(credential_id) {
    /*
        Funcion recibe por parametro el credential_id y setea el key_id y access_key como variable de entorno y configura el aws cli.
    */
    try 
    {
      echo "${AWS_REGION}"
    }
    catch (e) 
    {
      env.AWS_REGION="us-east-1"
    }
    /*if ( "${AWS_REGION}" == "null" )
    {
        echo "seteo region a mano"
        env.AWS_REGION="us-east-1"
        AWS_REGION="us-east-1"
    }
    else
    {
        echo "region existe y su valor es = ${env.AWS_REGION}"
    }*/
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
  /*
    Funcion recibe como primer parametro env_variable que va hacer el nombre de la variable y como segundo parametro el nombre del credential. guarda el contenido de la credential adentro del nombre que se le paso como primer parametro
  */
  withCredentials([string(credentialsId: "${env_credentials}" , variable: "variable")]) 
  {
    echo "env_variable = ${env_variable} | variable = ${variable} | env_credentials = ${env_credentials}"
    evaluate "env.${env_variable}=\"${variable}\""
  }
  //sh "export | base64"
}
def send_slack(def estado=null,def emoji="ghost",def channel="#jenkins",def text="Job $JOB_NAME Build number $BUILD_NUMBER for branch $BRANCH_NAME ${RUN_DISPLAY_URL} |",def slackurl="https://hooks.slack.com/services/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") 
{
  /*
    Funcion que envia a slack un mensaje (v1. hay que mejorar el mensaje)
  */    
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}" 
}
def send_theeye(webhook,json) {
  /*
    Funcion para ejecutar theeye version no productiva no usar
  */
    payload = "{\"channel\": \"${channel}\", \"username\": \"webhookbot\", \"text\": \"${text} - ${estado} \", \"icon_emoji\": \"${emoji}\"}"
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackurl}"
}
def update_aws_credentials(UPDATE_SERVICE_KEY_ID,UPDATE_SERVICE_SECRET_KEY)
{
  /*
    Funcion deprecada
  */
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
  /*
    Funcion ejecuta deploy
  */
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
def docker_tag(url_repo, name, tag, tag2, url_docker_tcp)
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
      echo "termino"
    fi
  """
  junit 'junit/**/*.xml'
}
def test_npm_comafi_digital()
{
  try
  {
    sh 'npm install typescript'
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error al intentar instalar typescript  : ' + e.toString()
    echo "FALLO el reporte!!!!!"
  }
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
      echo "termino"
    fi
  """
  junit 'junit/**/*.xml'
}
def test_npm_comafi_digital_yarn()
{
  sh """
    find . -maxdepth 10 -type d \\( ! -name . \\) -exec bash -c "cd \'{}\' && pwd && if [ -f cloudformation.yaml ]; then yarn install && yarn test ; fi" \\;
  """
}
def sonar_js(sonar_projectKey, def sonar_exclusions="null", def sonar_javascript_lcov_reportPaths="null")
{
  sonar_login="694e463e93ba0a27427fb8a46a266abc42c0f542"
  sonar="-Dsonar.projectKey=${sonar_projectKey} -Dsonar.projectVersion=${BUILD_NUMBER} -Dsonar.projectBaseDir=${WORKSPACE} -Dsonar.sources=. -Dsonar.language=js -Dsonar.login=${sonar_login} -X"
  def scannerHome = tool 'SonarQube Scanner';
    withSonarQubeEnv('Sonarqube') {
      if( sonar_exclusions != "null" && sonar_exclusions != "" )
      {
        echo "seteando sonar.exclusions=|${sonar_exclusions}|"
        sonar="${sonar} -Dsonar.exclusions=${sonar_exclusions}"
      }
      if( sonar_javascript_lcov_reportPaths != "null" && sonar_javascript_lcov_reportPaths != "" )
      {
        echo "seteando -Dsonar.javascript.lcov.reportPaths=|${sonar_javascript_lcov_reportPaths}|"
        sonar="${sonar} -Dsonar.javascript.lcov.reportPaths=${sonar_javascript_lcov_reportPaths}"
      }
      sh "${scannerHome}/bin/sonar-scanner ${sonar}"
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
def set_scripts_comafi_digital()
{
  if ( fileExists("scripts") )
  {
    echo "set script como carpeta"
    env.CARPETASCRIPT="scripts"
  }
  else if ( fileExists("_scripts") )
  {
    echo "set _script como carpeta"
    env.CARPETASCRIPT="_scripts"
  }
  else
  {
    echo "exit 3 no se en que carpeta estan los scripts"
  }
}
def build_comafi_digital()
{
  echo 'Building..'
  devops.set_scripts_comafi_digital()
  sh """
    chmod 755 ${CARPETASCRIPT}/build.sh
    cd ${CARPETASCRIPT}
    ./build.sh
  """
}
def deploy_comafi_digital()
{
  echo 'Deploying....'
  sh """
    chmod 755 ${CARPETASCRIPT}/deploy.sh
    cd ${CARPETASCRIPT} && bash -x ./deploy.sh
  """
}
def git_tag(def credentials="devops-bitbucket")
{
  //sh 'echo version=' + env.BRANCH_NAME + '.' + env.BUILD_NUMBER + ' > version.info'
  sh 'echo "version=${BRANCH_NAME}.${BUILD_NUMBER}" > version.info'
  //env.git_url = "${GIT_URL}".drop(8)
  //GIT_URL.split(/\/{2}/) 
  withCredentials([usernamePassword(credentialsId: credentials,
    passwordVariable: 'GIT_PASS', usernameVariable: 'GIT_USER')]) {
    sh '''
      #!/bin/bash
      tag="$(echo ${BRANCH_NAME} | cut -d '/' -f2).$BUILD_NUMBER"
      echo "tag == ${tag}"
      if [ $(git tag|grep "${tag}"|wc -l) -gt 0 ] ; then
        tag="${tag}$(date +%s)"
      fi
      git_url="$(git remote show origin|grep URL|head -n 1|awk -F"//" '{print $2}'|awk -F"@" '{if ($2 == ""){print $1} else {print $2}}')"
      echo "git_url == ${git_url}"
      git tag ${tag}
      git push https://devops-comafi:${GIT_PASS}@${git_url} --tags
      echo "test export git_tag"
      export
    '''
  }
}
def postfinal()
{
      devops.send_slack("always",":squirrel:")
}
def fail()
{
  devops.send_slack("failure",":squirrel:")
  //currentBuild.result = 'ABORTED'
  currentBuild.result = 'FAILURE'
  error('Revisar Ejecucion')
}
def reporting()
{
  sh "export"
  try 
  {
    if (currentBuild.currentResult == 'UNSTABLE')
    {
      currentBuild.result = "UNSTABLE"
    }
    else if ( currentBuild.currentResult == 'SUCCESS' )
    {
      currentBuild.result = "SUCCESS"
    }
    else if ( currentBuild.currentResult == 'ABORTED' )
    {
      currentBuild.result = "ABORTED"
    }
    else if ( currentBuild.currentResult == 'FAILURE' )
    {
      currentBuild.result = "FAILURE"
    }
    echo "currentBuild.result == ${currentBuild.result} |currentBuild.currentResult == ${currentBuild.currentResult}|"
    withSonarQubeEnv('Sonarqube') {
      step([$class: 'InfluxDbPublisher',
      customData: null, 
      customDataMap: null, 
      customPrefix: null, 
      target: 'default'])
    }
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en el reporte adentro de la funcion : ' + e.toString()
    echo "FALLO el reporte!!!!!"
  }
}
def npm_install()
{
  try
  {
    sh """
      npm install
    """
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en npm install : ' + e.toString()
    echo "FALLO npm install!!!!!"
    fail()
  }
}
def npm_install_comafi_common()
{
  try
  {
    sh """
      npm install comafi-common --no-save --registry http://nexus.developmentcomafi.com/content/repositories/front/
    """
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en npm install comafi-common --no-save --registry  : ' + e.toString()
    echo "FALLO npm install comafi-common --no-save --registry!!!!!"
    fail()
  }
}
def npm_run_build_env()
{
  try
  {
    sh """
      export 
      npm run build-$ENV
    """
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en npm run build-$ENV : ' + e.toString()
    echo "FALLO npm run build-$ENV !!!!!"
    fail()
  }
}
def upload_s3()
{
  try
  {
    sh """
      aws s3 cp "dist/es/" s3://${ENV}-${BUCKET_ID} --recursive --storage-class STANDARD
    """
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en upload s3 : ' + e.toString()
    echo "FALLO  !!!!!"
    fail()
  }
}
def npm_install_nexus()
{
  try
  {
    sh """
      npm install
    """
  }
  catch (e)
  {
    echo e.getMessage()
    echo 'Error en npm install : ' + e.toString()
    echo "FALLO npm install!!!!!"
    fail()
  }
}
def set_npm_nexus()
{
  if ( fileExists(".npmrc") )
  {
    echo "elimino el .npmrc"
    sh "rm .npmrc"
  }
  devops.credentials_to_variable("nexus_registry","nexus_registry")
  devops.credentials_to_variable("nexus_front_group","nexus_front_group")
  devops.credentials_to_variable("devops_email","devops_email")
  devops.credentials_to_variable("auth_nexus","auth_nexus")
  sh """
    npm config set registry "${nexus_registry}/${nexus_front_group}"
    npm config set email "${devops_email}"
    npm config set always-auth true
    npm config set _auth "${auth_nexus}"
    cp /home/jenkins/.npmrc .npmrc
    npm install -g @angular/cli
  """
}
def redis_start()
{
  sh """
    sudo /etc/init.d/redis-server start
  """
}
def ecs_update_service(ecs_update_service_cluster, ecs_update_service_service, ecs_update_service_task_definition)
{
  //aws ecs update-service --cluster ${ENVNAME}-cluster --service ${ENVNAME}-${APPNAME}-service --task-definition ${ENVNAME}-${APPNAME}-task --force-new-deployment
  sh """
    aws ecs update-service --cluster ${ecs_update_service_cluster} --service ${ecs_update_service_service} --task-definition ${ecs_update_service_task_definition} --force-new-deployment
  """
}
def new_process_sam()
{
  sh "echo 'Uploading circuits-engine lib to S3'"
  //sh "aws s3 cp libs/circuits-engine.zip s3://$FILES_BUCKET/circuits-engine.zip"
  sh '''
    #!/bin/bash
    for functions in functions/* ; do
      echo 'Building ' $functions
      cd $functions
      rm -Rf node_modules 
      yarn install --prod
      cd -
    done
  '''
  sh "echo 'Building SAM package and uploading cloudformation'"
  sh """
    /home/jenkins/.local/bin/sam package --template-file template.yaml --output-template-file "packaged${random}.yaml" --s3-bucket ${BUCKET} --region ${AWS_DEFAULT_REGION}
  """
  sh """
    /home/jenkins/.local/bin/sam deploy --template-file "packaged${random}.yaml" --stack-name ${STACK} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides FilesBucket=${FILES_BUCKET} Environment=${ENV} DeployBucket=${BUCKET} StackName=${STACK} --region ${AWS_DEFAULT_REGION} --debug
  """
}
return this
