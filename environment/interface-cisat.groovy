def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="comercios-frontend-boilerplate"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  /*env.URL_CISAT='http://localhost:5002'
  env.CANAL='AC'
  env.PERFIL='AC1'*/
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.tag="true"
    if ( cuenta == "_srv_jenkins_pec" )
    {
      devops.aws_config("_srv_jenkins_pec")
      env.ENV='prod'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      /*devops.credentials_to_variable("URL_CISAT","URL_CISAT_PROD")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_PROD")
      devops.credentials_to_variable("SUBNET1","SUBNET1_COMAFI_PROD")
      devops.credentials_to_variable("SUBNET2","SUBNET2_COMAFI_PROD")*/
      return false
    }
    else if ( cuenta == "AWS_Alternative" ) 
    {
      devops.aws_config("AWS_Alternative")
      env.ENV="prod"
      env.COST_CENTER="comafi_digital_prod"
      env.STACK_NAME="InterfaceCisatV2"
      devops.credentials_to_variable("URL_CISAT","URL_CISAT_PROD")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_PROD")
      devops.credentials_to_variable("SUBNET1","SUBNET1_COMAFI_PROD")
      devops.credentials_to_variable("SUBNET2","SUBNET2_COMAFI_PROD")
      return true
    }
    else
    {
      return false
    }
  }
  else if ( env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" || env.BRANCH_NAME == "impleqa" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.tag="true"
    if ( cuenta == "null" || cuenta == "AWS_QA_CMF" )
    {
      devops.aws_config("AWS_QA_CMF")
      env.ENV='qa'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      /*devops.credentials_to_variable("URL_CISAT","URL_CISAT_QA")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_QA")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_QA1")
      devops.credentials_to_variable("SUBNET2","SUBNET_COMAFI_QA2")*/
      return false
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='dtkqa'
      env.STACK_NAME='InterfaceCisatV2'
      devops.credentials_to_variable("URL_CISAT","URL_CISAT_QA")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_QA")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_QA1")
      devops.credentials_to_variable("SUBNET2","SUBNET_COMAFI_QA2")
      return true
    }
    else
    {
      return false
    }    
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      devops.aws_config("AWS_DESA_CMF")
      //devops.credentials_to_variable("URL_CISAT_DEV","URL_CISAT_DEV")
      env.ENV='dev'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      /*devops.credentials_to_variable("URL_CISAT","URL_CISAT_DEV")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_DEV")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_DEV1")
      devops.credentials_to_variable("SUBNET2","SUBNET_COMAFI_DEV2")*/
      return false
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      //devops.credentials_to_variable("SECURITY_GROUP_COMAFI_DEV","SECURITY_GROUP_COMAFI_DEV")
      env.ENV='dtkdev'
      env.COST_CENTER='comercios_dev'
      env.STACK_NAME='InterfaceCisatV2'
      devops.credentials_to_variable("URL_CISAT","URL_CISAT_DEV")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_DEV")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_DEV1")
      devops.credentials_to_variable("SUBNET2","SUBNET_COMAFI_DEV2")
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      devops.aws_config("AWS_DESA_CMF")
      env.ENV='predev'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      /*devops.credentials_to_variable("URL_CISAT","URL_CISAT_DEV")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_DEV")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_DEV1")
      devops.credentials_to_variable("SUBNET2","SUBNETD_COMAFI_DEV2")*/
      return false
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='dtkpredev'
		  env.STACK_NAME='InterfaceCisatV2'
      devops.credentials_to_variable("URL_CISAT","URL_CISAT_DEV")
      devops.credentials_to_variable("CANAL","CANAL")
      devops.credentials_to_variable("PERFIL","PERFIL")
      devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_DEV")
      devops.credentials_to_variable("SUBNET1","SUBNET_COMAFI_DEV1")
      devops.credentials_to_variable("SUBNET2","SUBNET_COMAFI_DEV2")
      return true
    }
    else
    {
      return false
    }
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
