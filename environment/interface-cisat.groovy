def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="comercios-frontend-boilerplate"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  env.URL_CISAT='http://localhost:5002'
  env.CANAL='AC'
  env.PERFIL='AC1'
  env.REDIS_URL='clusterusersessiontokens.zlslo8.ng.0001.use2.cache.amazonaws.com'
  env.REDIS_PORT=6379
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    //env.tag="true"
    if ( cuenta == "_srv_jenkins_pec" )
    {
      devops.aws_config("_srv_jenkins_pec")
      env.ENV='prod'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')  
      return true
    }
    else if ( cuenta == "AWS_Alternative" ) 
    {
      devops.aws_config("AWS_Alternative")
      env.ENV='prod'
      env.COST_CENTER='comafi_digital_prod'
      env.STACK_NAME='CMF-DIGITAL-INTERFACE-CISAT'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
      return true
    }
    else
    {
      return false
    }
  }
  else if ( env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.tag="true"
    if ( cuenta == "null" || cuenta == "AWS_QA_CMF" )
    {
      devops.aws_config("AWS_QA_CMF")
      env.ENV='qa'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='dtkqa'
      env.STACK_NAME='INTERFACE-CISAT'
      env.AWS_DEFAULT_REGION='us-east-1'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
      return true
    }
    else
    {
      return false
    }    
  }
  else if (env.BRANCH_NAME == "impleqa" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.tag="true"
    if ( cuenta == "null" || cuenta == "AWS_QA_CMF" )
    {
      devops.aws_config("AWS_QA_CMF")
      env.ENV='qa'
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='qa'
      env.COST_CENTER='comafi_digital_qa'
      env.STACK_NAME='PD-INTERFACE-CISAT'
      env.AWS_DEFAULT_REGION='us-east-1'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
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
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      //devops.credentials_to_variable("SECURITY_GROUP_COMAFI_DEV","SECURITY_GROUP_COMAFI_DEV")
      env.ENV='dtkdev'
      env.COST_CENTER='comercios_dev'
      env.STACK_NAME='INTERFACE-CISAT'
      env.AWS_DEFAULT_REGION='us-east-1'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP=credentials('SECURITY_GROUP')
      env.SUBNET1=credentials('SUBNET1')
      env.SUBNET2=credentials('SUBNET2')
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
      env.COST_CENTER='ComafiDigital'
      env.STACK_NAME='CmfDigitalInterfaceCisat'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP = credentials('SECURITY_GROUP')
      env.SUBNET1 = credentials('SUBNET1')
      env.SUBNET2 = credentials('SUBNET2')
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='dtkpredev'
      env.COST_CENTER='comercios_dev'
      env.STACK_NAME='INTERFACE-CISAT'
      env.URL_CISAT=credentials('URL_CISAT')
      env.CANAL=credentials('CANAL')
      env.PERFIL=credentials('PERFIL')
      env.SECURITY_GROUP = credentials('SECURITY_GROUP')
      env.SUBNET1 = credentials('SUBNET1')
      env.SUBNET2 = credentials('SUBNET2')
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
