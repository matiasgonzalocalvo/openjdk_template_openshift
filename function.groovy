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
def maven_cobertura(def settings="null")
{
    /*
        Funcion que ejecuta maven verify recibe el archivo settings.xml de fomar opcional si no se pasa se ejecuta sin el parametro -s.
    */
    if ( settings == "null" )
    {
        sh "mvn cobertura:cobertura -DskipTests -X"
    }
    else
    {
        sh "mvn cobertura:cobertura -DskipTests -s ${settings} -X"
    }
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
def maven_release_prepare(def settings="null")
{
  /*
    Funcion ejecuta deploy
  */
  if ( settings == "null" )
  {   
    sh "mvn release:prepare -DskipTests -X"
  }
  else
  {
    sh "mvn release:prepare -DskipTests -s ${settings} -X"
  }
}
def maven_release_perform(def settings="null")
{
  /*
    Funcion ejecuta deploy
  */
  if ( settings == "null" )
  {  
    sh "mvn release:perform -DskipTests -X -B"
  }
  else
  {
    sh "mvn release:perform -DskipTests -s ${settings} -X -B"
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
def docker_delete(def url_repo="null",def name="null",def tag="null",def url_docker_tcp="null")
{
    sh """ 
        docker -H "${url_docker_tcp}" rmi  ${url_repo}/${name}:${tag} 
    """
}
def jenkins_docker_build(def url_repo="null", def name="null",def tag="null",def url_docker_tcp="null")
{
    sh "echo ejecutando docker usando plugin de jenkins"
    app = docker.build("${url_repo}/${name}:${tag}")
}
def docker_tag(url_repo, name, tag, tag2, url_docker_tcp, def url_repo2="null")
{
  if ( url_repo2 == "null" )
  {
    url_repo2="${url_repo}"
  }
  sh """ 
    docker -H "${url_docker_tcp}" tag  ${url_repo}/${name}:${tag} ${url_repo2}/${name}:${tag2}
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
def lambda_sonar(sonar_projectKey, def sonar_exclusions="null", def sonar_javascript_lcov_reportPaths="null")
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
      sh '''
        #!/bin/bash
        for functions in functions/* ; do
          echo 'Test In ' $functions
          cd $functions
          ${scannerHome}/bin/sonar-scanner ${sonar}
          cd -
        done
      '''
    }
}
def wait_sonar()
{
    timeout(time: 1, unit: 'HOURS')
    {
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
    echo "no existe scripts ni _sscripts"
    //env.CARPETASCRIPT="null"
    env.CARPETASCRIPT="."
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
      if [ $(git tag|grep "${tag}"|wc -l) -gt 0 ] ; then
        tag="${tag}$(date +%s)"
      fi
      git_url="$(cat .git/config |grep "url"| awk -F"//" '{print $2}')"
      git tag ${tag}
      git push https://devops-comafi:${GIT_PASS}@${git_url} --tags
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
def npm_run_build_pkg()
{
  sh '''
    npm run build-pkg
  '''
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

def set_nexus_login()
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
  """
}

def set_npm_nexus_publish()
{
  if ( fileExists(".npmrc") )
  { 
    echo "elimino el .npmrc"
    sh "rm .npmrc"
  }
  devops.credentials_to_variable("nexus_registry","nexus_registry")
  devops.credentials_to_variable("content_repositories_front","content_repositories_front")
  devops.credentials_to_variable("devops_email","devops_email")
  devops.credentials_to_variable("auth_nexus","auth_nexus")
  sh '''
    #!/bin/bash
    npm config set registry "${nexus_registry}/${content_repositories_front}"
    npm config set email "${devops_email}"
    npm config set always-auth true
    npm config set _auth "${auth_nexus}"
    if [ -e "package-common.json" ] ; then
      mv package-common.json lib/package.json
    fi
    if [ -e ".npmrc-common" ] ; then
      mv .npmrc-common lib/.npmrc
    fi
    cp /home/jenkins/.npmrc .npmrc
    cp /home/jenkins/.npmrc lib/
    cd lib/
    npm publish
  '''
}
def set_npm_nexus_publish_lib()
{
  if ( fileExists(".npmrc") )
  { 
    echo "elimino el .npmrc"
    sh "rm .npmrc"
  }
  devops.credentials_to_variable("nexus_registry","nexus_registry")
  //devops.credentials_to_variable("content_repositories_front","content_repositories_front")
  devops.credentials_to_variable("devops_email","devops_email")
  devops.credentials_to_variable("auth_nexus","auth_nexus")
  sh '''
    #!/bin/bash
    npm config set registry "${nexus_registry}/${content_repositories_front}"
    npm config set email "${devops_email}"
    npm config set always-auth true
    npm config set _auth "${auth_nexus}"
    npm publish /dis/*
  '''
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
      chmod -R +x *
      cd -
    done
  '''
  sh "echo 'Building SAM package and uploading cloudformation'"
  sh """
    sam package --template-file template.yaml --output-template-file "packaged${random}.yaml" --s3-bucket ${BUCKET} --region ${AWS_DEFAULT_REGION}
  """
  sh """
    sam deploy --template-file "packaged${random}.yaml" --stack-name ${STACK} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides FilesBucket=${FILES_BUCKET} Environment=${ENV} DeployBucket=${BUCKET} StackName=${STACK} DefaultFiles=${DEFAULT_BUCKET} ThubanHost=${ThubanHost} ThubanPassword=${ThubanPassword} ThubanUser=${ThubanUser} --region ${AWS_DEFAULT_REGION} --debug
  """
}
def yarn_install_functions()
{
  sh '''
    #!/bin/bash
    for functions in functions/* ; do
      echo 'Building ' $functions
      cd $functions
      if [ -e node_modules ] ; then
        rm -Rf node_modules 
      fi
      yarn install --prod
      cd -
    done
  '''
}
def yarn_install_lib()
{
  sh '''
    #!/bin/bash
    for proyects in proyects/* ; do
      echo 'Building ' $proyects
      cd $proyects
      yarn run
      cd -
    done
  '''
}
def lambda_yarn_install()
{
  sh '''
    #!/bin/bash
    for functions in ${SOURCE_functions}/* ; do
      echo 'Building ' $functions
      cd $functions
      if [ -e node_modules ] ; then
        rm -Rf node_modules 
      fi
      yarn install --prod
      cd -
    done
  '''
}

def lambda_yarn_test()
{
  sh '''
    #!/bin/bash
    for functions in ${SOURCE_functions}/* ; do
      echo 'Test In ' $functions
      cd $functions
      if [ -e node_modules ] ; then
        rm -Rf node_modules 
      fi
      yarn test
      cd -
    done
  '''
}
def swagger_cp_s3()
{
  echo 'Uploading swagger to S3'
  sh '''
    aws s3 cp "${SOURCE_cloudformation}/swagger.yaml" s3://$BUCKET/$ENV-swagger-${random}.yaml
  '''
}
def sam_package()
{
  // seteo la variable carpetascript que voy a usar abajo
  devops.set_scripts_comafi_digital()
  sh '''
    sam package --template-file ${SOURCE_functions}/template.yaml --output-template-file "${SOURCE_functions}/packaged${random}.yaml" --s3-bucket ${BUCKET} --region ${AWS_DEFAULT_REGION} --debug
  '''
}
def lambda_sam_package()
{
  // seteo la variable carpetascript que voy a usar abajo
  sh '''
    #/bin/bash
    sam package --template-file ${SOURCE_cloudformation}/template.yaml --output-template-file "${SOURCE_cloudformation}/packaged${random}.yaml" --s3-bucket ${BUCKET} --region ${AWS_DEFAULT_REGION} --debug
  '''
}

def lambda_sam_package_security()
{
  // seteo la variable carpetascript que voy a usar abajo
  sh '''
    #/bin/bash
    sam package --template-file "${SOURCE_cloudformation}/security.yaml" --output-template-file "${SOURCE_cloudformation}/security_${random}.yaml" --s3-bucket $BUCKET --debug
  '''
}


def sam_deploy( def overrides="null" )
{
  if ( overrides == "null" ) 
  {
    sh '''
      sam deploy --template-file "${SOURCE_functions}/packaged${random}.yaml" --stack-name ${STACK} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides ${parameter_overrides} --region ${AWS_DEFAULT_REGION} --debug --no-fail-on-empty-changeset 
    '''
  }
  else
  {
    sh '''
      sam deploy --template-file "packaged${random}.yaml" --stack-name ${STACK} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides ${overrides} --region ${AWS_DEFAULT_REGION} --debug --no-fail-on-empty-changeset 
    '''
  }
}
def lambda_sam_deploy()
{
    sh '''
      sam deploy --template-file "${SOURCE_cloudformation}/packaged${random}.yaml" --stack-name ${STACK} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides ${parameter_overrides} --region ${AWS_DEFAULT_REGION} --debug --no-fail-on-empty-changeset 
    '''
}

def lambda_sam_deploy_security()
{
    sh '''
      sam deploy --template-file "${SOURCE_cloudformation}/security_${random}.yaml" --stack-name ${STACK_SECURITY} --tags Project=${PROJECT} --capabilities CAPABILITY_NAMED_IAM --parameter-overrides ${parameter_overrides_security} --region ${AWS_DEFAULT_REGION} --debug --no-fail-on-empty-changeset 
    '''
}


def config_file_provider(fileid, def settings="null")
{
  if ( settings == "null" )
  {
    settings = "settings.xml"
  }
  configFileProvider(
    [ configFile(fileId: fileid, variable: 'MAVEN_SETTINGS')]) 
    {
      sh "cp ${MAVEN_SETTINGS} ${WORKSPACE}/${settings}"
      env.settings="${WORKSPACE}/${settings}"
      sh "cat ${WORKSPACE}/${settings}"
    }
}
def reltag()
{
  env.TAG1 = sh(script: ''' git tag | xargs -I@ git log --format=format:"%ai @%n" -1 @ | sort | awk '{print $4}' | tail -n1 ''', returnStdout: true).trim()
  sh '''
    #!/bin/bash
    RELTAG=$(git tag | xargs -I@ git log --format=format:"%ai @%n" -1 @ | sort | awk '{print $4}' | tail -n1)
    echo "TAG1=$RELTAG" > promotag
    git checkout $RELTAG
  '''
}


def ng_build()
{
  sh '''
    #!/bin/bash
    npm install
    ng build 
  '''
}


def ng_test()
{
  sh '''
    #!/bin/bash
    echo "npm install"
    npm install
    echo "ng test"
    ng test 
  '''
}
def cloudfront_invalidation()
{
  sh '''
    #!/bin/bash
    CLOUDFRONT_DISTRIBUTION=$(aws cloudformation describe-stacks --stack-name "${STACK}"  --output text | grep ${ENV}-${Cloudfront_distribution} | awk -F"\t" '{$0=$5}6')
    aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DISTRIBUTION} --path "/*"
  '''
}
def front_s3_cp()
{
  sh '''
    aws s3 cp dist/comafi/ s3://"${ENV}${SUBDOMINIO}.${DOMAIN}"/  --acl public-read --recursive
  '''
}
def front_s3_cp_index()
{
  sh '''
    aws s3 cp dist/comafi/index.html s3://"${ENV}${SUBDOMINIO}.${DOMAIN}"/  --acl public-read  --cache-control max-age=3600
  '''
}
def create_repository(def repository_name='null')
{
  env.repository_name="${repository_name}"
  try 
  {
    sh '''
      #!/bin/bash
      aws ecr describe-repositories --repository-names ${repository_name} 2>&1 > /dev/null
    '''
  }
  catch (e)
  {
    echo "no existe el repositorio lo creo"
    sh '''
      #!/bin/bash
      aws ecr create-repository --repository-name ${repository_name}
    '''
  }
}
def check_commit(CC_GREP) 
{
  env.CC_GREP="${CC_GREP}"
//  result = sh (script: ''' git log -1 | grep "${CC_GREP}"|wc -l ''', returnStatus: true)
  result = sh (script: ''' git log -1 | grep "${CC_GREP}" ''', returnStatus: true)
  if (result == 1) 
  {
    sh ''' git log -1  ''' 
    echo "${CC_GREP} Not found in git commit message. || result == ${result} "
    return false
  }
  else if (result == 0 )
  {
    echo "${CC_GREP} found in git commit message. || result == ${result}"
    return true
  }
  else
  {
    echo "salida distinta de 0 || result == ${result}"
  }
}
def circuit_creator(create)
{
  if ( create == "true") 
  {
    sh '''
      #!/bin/bash
      #set +x
      curl -f --header "Content-Type: application/json" \
      -H "Authorization: Bearer ${TOKEN}" \
      --request POST \
      --data '{"region": "'"${AWS_DEFAULT_REGION}"'", "environment": "'"${ENV}"'", "repository_owner_account": "comafi", "circuit_repository_name": "'"${repoName}"'", "target_branch": "'"${BRANCH_NAME}"'", "repo_user": "'"${devops_comafi}"'", "repo_pass": "'"${devops_password}"'"}' \
      ${url_circuit_tables}
    '''
  }
  else if ( create == "fill" )
  {
    sh '''
      #!/bin/bash
      #set +x
      curl -f --header "Content-Type: application/json" \
      -H "Authorization: Bearer ${TOKEN}" \
      --request POST \
      --data '{"region": "'"${AWS_DEFAULT_REGION}"'", "environment": "'"${ENV}"'", "stackname": "'"${STACK}"'", "repository_owner_account": "comafi", "circuit_repository_name": "'"${repoName}"'", "target_branch": "'"${BRANCH_NAME}"'", "repo_user": "'"${devops_comafi}"'", "repo_pass": "'"${devops_password}"'"}' \
      ${url_fill_circuit_tables}
    '''
  }
  else
  {
    echo "CRITICAL no se paso argumento valido"
    fail()
  }
}
def update_version()
{
  sh '''
    #!/bin/bash
    version_txt="version.txt"
    echo "$(date) : Version actual : $(cat ${version_txt})"
    if [ -e ${version_txt} ] ; then
      echo "$(date) : Actualizando version"
      echo ${new_version}  > ${version_txt}
      echo "$(date) : Version New $(cat ${version_txt})"
    fi
  '''
}
def main()
{
  try
  {
    if ( "${JenkinsVersion}" == "1.0" )
    {
      echo "\u2600 Version de Jenkinsfile OK \u263A"
    }
  }
  catch (e)
  {
    echo "\u274C version de Jenkinsfile desactualizada \u2639 \u2622"
  }
}
main()
return this
