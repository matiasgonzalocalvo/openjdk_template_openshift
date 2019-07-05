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
    env.ENV='predev'
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.STACK="${ENV}-INFRA-OPEN-CE-APJ-API"
    env.PROJECT="OPEN-CE-APJ-API"
    //VARIABLES ESPECIFICAS SEGUN EL MICROSERVICIO
    env.SECURITY_GROUP_ID_1="sg-07d08c61cd167f619"
    env.SUBNET_ID_1="subnet-0a51d13e6bb796d09"
    env.SUBNET_ID_2="subnet-0d87fb44935717d2e"
    env.CISAT_URL="https://89gzrrzn8k.execute-api.us-east-1.amazonaws.com/dev/interface-cisat"
    env.CISAT_TOKEN="bnmu6WzW619tnH7jnmfsv2tAsm8Ddzikoq4u6876"
    env.parameter_overrides="UUID=${random} Environment=${ENV} DeployBucket=${BUCKET} StackName=${STACK} SecurityGroupId1=${SECURITY_GROUP_ID_1} SubnetId1=${SUBNET_ID_1} SubnetId2=${SUBNET_ID_2} CisatUrl=${CISAT_URL} CisatToken=${CISAT_TOKEN}"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA_CMF")
    //devops.aws_config("AWS_DESA")
    env.ENV='predev'
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.STACK="${ENV}-INFRA-OPEN-CE-APJ-API"
    env.PROJECT="OPEN-CE-APJ-API"
    // VARIABLES ESPECIFICAS SEGUN EL MICROSERVICIO
    env.SECURITY_GROUP_ID_1="sg-07d08c61cd167f619"
    env.SUBNET_ID_1="subnet-0a51d13e6bb796d09"
    env.SUBNET_ID_2="subnet-0d87fb44935717d2e"
    env.CISAT_URL="https://89gzrrzn8k.execute-api.us-east-1.amazonaws.com/dev/interface-cisat"
    env.CISAT_TOKEN="bnmu6WzW619tnH7jnmfsv2tAsm8Ddzikoq4u6876"
    env.parameter_overrides="UUID=${random} Environment=${ENV} DeployBucket=${BUCKET} StackName=${STACK} SecurityGroupId1=${SECURITY_GROUP_ID_1} SubnetId1=${SUBNET_ID_1} SubnetId2=${SUBNET_ID_2} CisatUrl=${CISAT_URL} CisatToken=${CISAT_TOKEN}"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
