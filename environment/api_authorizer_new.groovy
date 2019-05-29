def setenv(def cuenta="null")
{
    /*
      Variables Globales a todos los ambientes
    */
    env.sonar_projectKey="AUTH"
    env.sonar_exclusions="test/**,/functions/AUTHORIZER/security_realms/circuit*,functions/AUTHORIZER/test/mocks_azure_ad/mocked_jsonwebtoken.js,functions/AUTHORIZER/cognito*,/test/*/**,test/*,/test/*,/test/*/**,test/*,/test/**/*,/**/test/*,scripts/*,/**/build/*,**/.nyc_output/*,**/node_modules/*.test-reports/*,coverage/**/*,/**/coverage/**/*,/coverage/**/*,.coverage,**/scripts/*"
    env.sonar_javascript_lcov_reportPaths="$WORKSPACE/coverage/AUTHORIZER/lcov.info"
    env.AWS_DEFAULT_REGION='us-east-1'
    /*
    */    
    if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
    {
        devops.aws_config("_srv_jenkins_pec")
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        env.tag="true"
        env.ENV='prod'
        env.COST_CENTER='comafi_digital_prod'
        env.FILES_BUCKET='cmf-comercios-sec-files'
        envSTACK_NAME='Authorizer'
    }
    else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-comercios-sec-files'
        env.STACK_NAME='Authorizer'
    }
    else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='predev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-comercios-sec-files'
        env.STACK_NAME='Authorizer'
    }
    else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.tag="true"
        env.ENV='qa'
        env.COST_CENTER='comafi_digital_qa'
        env.FILES_BUCKET='cmf-comercios-sec-files'
        env.STACK_NAME='Authorizer'
    }
    else if ( env.BRANCH_NAME =~ "test-flow" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        def env.list_counts=["AWS_DESA_CMF", "AWS_DESA"]
        //devops.aws_config("AWS_DESA_CMF")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-comercios-sec-files'
        env.STACK_NAME='Authorizer'
        if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
        {
          echo "cuenta == ${cuenta} "
          devops.aws_config("AWS_DESA_CMF")
        }
        else if ( cuenta == "AWS_DESA" )
        {
          echo "cuenta == ${cuenta} "
          devops.aws_config("AWS_DESA")
        }
        else
        {
          echo "FALLO EL SETEO DE VARIABLES !!!!!"
          devops.fail()
        }

    }
    else
    {
        echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
        sh "abort"
    }
}
return this;
