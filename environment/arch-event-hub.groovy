def set_env_global()
{
    /*
      Variables Globales a todos los ambientes
    */
    env.sonar_projectKey="ARCH-event-hub"
    env.sonar_exclusions="test/**,/functions/AUTHORIZER/security_realms/circuit*,functions/AUTHORIZER/test/mocks_azure_ad/mocked_jsonwebtoken.js,functions/AUTHORIZER/cognito*,/test/*/**,test/*,/test/*,/test/*/**,test/*,/test/**/*,/**/test/*,scripts/*,/**/build/*,**/.nyc_output/*,**/node_modules/*.test-reports/*,coverage/**/*,/**/coverage/**/*,/coverage/**/*,.coverage,**/scripts/*"
    env.sonar_javascript_lcov_reportPaths="$WORKSPACE/coverage/AUTHORIZER/lcov.info"
    env.AWS_DEFAULT_REGION='us-east-1'
    env.COST_CENTER='comafi_arquitectura_aplicativa'
    env.FILES_BUCKET='cmf-event-hub-files'
}
def setenv(def cuenta="null")
{
    if (env.BRANCH_NAME == "master")
    {
       sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
       env.tag="true"
       
	   env.ENV='prod'
       env.STACK_NAME='EventHub'
       devops.aws_config("_srv_jenkins_pec")
       return true
    }
    else if (env.BRANCH_NAME =~ "release/*" || env.BRANCH_NAME =~ "hotfix/*" )
    {
      sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        
		env.ENV='qa'
        env.STACK_NAME='EventHub'
        devops.aws_config("AWS_DESA")
        return true
    }
    else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        
		env.ENV='dev'
        env.STACK_NAME='EventHub'
        devops.aws_config("AWS_DESA")
        return true
    }
    else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "bugfix/*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        
		env.ENV='predev'
        env.STACK_NAME='EventHub'
        devops.aws_config("AWS_DESA")
        return true
    }
    else if ( env.BRANCH_NAME =~ "test-flow" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
        {
          env.ENV='dev'
          env.STACK_NAME='Authorizer'
          devops.aws_config("AWS_DESA_CMF")
          return true
        }
        else if ( cuenta == "AWS_DESA" )
        {
          env.ENV='dtkdev'
          env.STACK_NAME='AUTHORIZER'
          devops.aws_config("AWS_DESA")
          return true
        }
        else
        {
          echo "return false"
          return false
        }
    }
    else
    {
        echo "ERROR: No entro a ninguna condicion de branch = ${env.BRANCH_NAME}"
        devops.fail()
    }
}
return this;
