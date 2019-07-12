def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="${repoName}"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  //env.AWS_PROFILE="default"
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_Alternative")
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA_CMF")
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA_CMF")
    env.ENV='dev'
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.STACK="${ENV}-INFRA-QUERY-CIRCUIT-SERVICE"
    env.STACK_SECURITY="${ENV}-SECURITY-QUERY-CIRCUIT-SERVICE"
    env.PROJECT=" QUERY-CIRCUIT-SERVICE"
    //VARIABLES ESPECIFICAS SEGUN EL MICROSERVICIO
    env.ES_NODE="https://search-dtkpredev-pec-elasticsearch-3etjrzdcpqpkagnoi6f26m4lrm.us-east-1.es.amazonaws.com"
    env.TENANT="tenant1"
    env.FILES_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ROLE_NAME="QueryCircuitServiceRole"
    env.parameter_overrides="UUID=${random} Environment=${ENV} DeployBucket=${BUCKET} EsNode=${ES_NODE} FilesBucket=${FILES_BUCKET} Tenant=${TENANT} RoleName=${ROLE_NAME}"
    env.parameter_overrides_security="Environment=${ENV} RoleName=${ROLE_NAME}"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA_CMF")
    //devops.aws_config("AWS_DESA")
    env.ENV='predev'
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.STACK="${ENV}-INFRA-QUERY-CIRCUIT-SERVICE"
    env.STACK_SECURITY="${ENV}-SECURITY-QUERY-CIRCUIT-SERVICE"
    env.PROJECT="QUERY-CIRCUIT-SERVICE"
    //VARIABLES ESPECIFICAS SEGUN EL MICROSERVICIO
    env.ES_NODE="https://search-dtkpredev-pec-elasticsearch-3etjrzdcpqpkagnoi6f26m4lrm.us-east-1.es.amazonaws.com"
    env.TENANT="tenant1"
    env.FILES_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ROLE_NAME="QueryCircuitServiceRole"
    env.parameter_overrides="UUID=${random} Environment=${ENV} DeployBucket=${BUCKET} EsNode=${ES_NODE} FilesBucket=${FILES_BUCKET} Tenant=${TENANT} RoleName=${ROLE_NAME}"
    env.parameter_overrides_security="Environment=${ENV} RoleName=${ROLE_NAME}"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
