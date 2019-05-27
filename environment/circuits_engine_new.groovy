def setenv()
{
    echo "funcion setenv"
    /*
      Variables Globales a todos los ambientes
    */
    env.sonar_projectKey="circuits-engine"
    env.sonar_exclusions="/test/**/*,/**/test/*,scripts/*,/**/build/*,**/.nyc_output/*,**/node_modules/*.test-reports/*,coverage/**/*,/**/coverage/**/*,/coverage/**/*,.coverage,**/scripts/*,/test/*/**,test/search/*.js,/test/search/*.js,/libs/circuits-engine/test/search/test-search.js,/libs/circuits-engine/test/files-management/*,/libs/circuits-engine/test/tasks/*,/libs/circuits-engine/test/user-profile/*,/libs/circuits-engine/test/tasks/detail/*,/libs/circuits-engine/test/comments/*"
    env.sonar_javascript_lcov_reportPaths="$WORKSPACE/coverage/CIRCUITS_ENGINE_CLAIM_TASK_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_QUERY_TASKS/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_QUERY_CIRCUITS/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_QUERY_CIRCUITS/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASK_REASSIGN/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASK_CLAIM_DETAIL/lcov.info,$WORKSPACE/coverage/circuits-engine-api-helper/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASK_DEFINITION_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CIRCUIT_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CLAIM_TASK/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_START_CIRCUIT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CREATE_CIRCUIT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CIRCUIT_DEFINITION_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASKS_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASK_RELEASE_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASK_DEFINITION_DETAIL/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_BATCH_TASKS_DISTRIBUTOR/lcov.info,$WORKSPACE/coverage/circuits-engine/lcov.info,$WORKSPACE/coverage/circuits-engine1/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_UPDATE_CIRCUIT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_DYNAMO_TRIGGER/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_ATTACH_DOCUMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_QUERY_DOCUMENT_ATTACHMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CONFIG_DOTME/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CIRCUITS_ADD_COMMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASKS_ADD_COMMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_NODE_REST_CLIENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_COMPLETE_TASK/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_CIRCUIT_DEFINITION_START_CIRCUIT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASKS_DOCUMENT_ATTACHMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_TASKS_QUERY_DOCUMENT_ATTACHMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_UPDATE_TASK/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_QUERY_ATTACHMENT/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_ASYNC_TASKS_EXECUTOR/lcov.info,$WORKSPACE/coverage/CIRCUITS_ENGINE_SEARCH_ATTACHMENT/lcov.info"
    env.AWS_DEFAULT_REGION='us-east-1'
    /*
    */    
    if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("_srv_jenkins_pec")
        env.tag="true"
        env.ENV='prod'
        env.COST_CENTER='comafi_digital_prod'
        env.FILES_BUCKET='prod-comercios-deploy'
        env.STACK_NAME='CircuiEngine'
        
        devops.credentials_to_variable("WHITELIST_BUCKET","BUCKET_WHITELIST_PROD")
        devops.credentials_to_variable("TASKS_QUEUE_URL","SQS_URL_PROD")
        devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_PROD")
        devops.credentials_to_variable("SECURITY_GROUP_PRIV","SECURITY_GROUP_PRIV_PROD")
        devops.credentials_to_variable("SUBNET1","SUBNETPROD1")
        devops.credentials_to_variable("SUBNET2","SUBNETPROD2")
        devops.credentials_to_variable("SUBNETPRIV1","SUBNETPRIVPROD1")
        devops.credentials_to_variable("ELASTICSEARCH_NAME","ELASTICSEARCH_NAME")
        devops.credentials_to_variable("ELASTICSEARCH_URL","ELASTICSEARCH_URL_PROD")

    }
    else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-comercios-files'
        env.STACK_NAME='CircuiEngine'
        
        devops.credentials_to_variable("WHITELIST_BUCKET","BUCKET_WHITELIST_DEV")
        devops.credentials_to_variable("TASKS_QUEUE_URL","SQS_URL_DEV")
        devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_DEV")
        devops.credentials_to_variable("SECURITY_GROUP_PRIV","SECURITY_GROUP_PRIV_DEV")
        devops.credentials_to_variable("SUBNET1","SUBNETDEV1")
        devops.credentials_to_variable("SUBNET2","SUBNETDEV2")
        devops.credentials_to_variable("SUBNETPRIV1","SUBNETPRIVDEV1")
        devops.credentials_to_variable("ELASTICSEARCH_NAME","ELASTICSEARCH_NAME")
        devops.credentials_to_variable("ELASTICSEARCH_URL","ELASTICSEARCH_URL_DEV")
    }
    else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*" || env.BRANCH_NAME == "test-flow1")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='predev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-comercios-files'
        env.STACK_NAME='CircuitEngine'
        
        devops.credentials_to_variable("WHITELIST_BUCKET","BUCKET_WHITELIST_PREDEV")
        devops.credentials_to_variable("TASKS_QUEUE_URL","SQS_URL_PREDEV")


        //devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_DEV")
        //devops.credentials_to_variable("SECURITY_GROUP_PRIV","SECURITY_GROUP_PRIV_DEV")
        devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_COMAFI_DIGITAL_DEV")

        //devops.credentials_to_variable("SUBNET1","SUBNETDEV1")
        //devops.credentials_to_variable("SUBNET2","SUBNETDEV2")
        //devops.credentials_to_variable("SUBNETPRIV1","SUBNETPRIVDEV1")
        devops.credentials_to_variable("SUBNET1","SUBNETDEV1-COMAFI-DIGITAL-DEV")
        devops.credentials_to_variable("SUBNET2","SUBNETDEV2-COMAFI-DIGITAL-DEV")

        //devops.credentials_to_variable("ELASTICSEARCH_NAME","ELASTICSEARCH_NAME")
        //devops.credentials_to_variable("ELASTICSEARCH_URL","ELASTICSEARCH_URL_PREDEV")
        devops.credentials_to_variable("ELASTICSEARCH_NAME","ELASTICSEARCH_NAME-COMAFI-DIGITAL-DEV")
        //devops.credentials_to_variable("ELASTICSEARCH_URL","ELASTICSEARCH_URL_PREDEV")
    }
    else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.tag="true"
        env.ENV='qa'
        env.COST_CENTER='comafi_digital_qa'
        env.FILES_BUCKET='qa-comercios-deploy'
        env.STACK_NAME='CircuiEngine'
        
        devops.credentials_to_variable("WHITELIST_BUCKET","BUCKET_WHITELIST_QA")
        devops.credentials_to_variable("TASKS_QUEUE_URL","SQS_URL_QA")
        devops.credentials_to_variable("SECURITY_GROUP","SECURITY_GROUP_QA")
        devops.credentials_to_variable("SECURITY_GROUP_PRIV","SECURITY_GROUP_PRIV_QA")
        devops.credentials_to_variable("SUBNET1","SUBNETQA1")
        devops.credentials_to_variable("SUBNET2","SUBNETQA2")
        devops.credentials_to_variable("SUBNETPRIV1","SUBNETPRIVQA1")
        devops.credentials_to_variable("ELASTICSEARCH_NAME","ELASTICSEARCH_NAME")
        devops.credentials_to_variable("ELASTICSEARCH_URL","ELASTICSEARCH_URL_QA")
    }
    else
    {
        devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
        echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
        sh "abort"
    }
}
return this;
