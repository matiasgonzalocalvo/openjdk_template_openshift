#!groovy
def flujo() 
{
  node_docker="jenkins-slave-comafi-nodejsdtk"
  try
  {
    loadvar.set_env_global()
    print node_docker 
  }
  catch (e)
  {
    print e.getMessage()
    print 'Err:  ' + e.toString()
    echo "no existe set_env_global"
  }
  node ("${node_docker}")
  {
    pipeline 
    {
      try 
      {
        stage('Checkout SCM')
        {
          checkout scm
        }
        stage('set env')
        {
          try
          {
            loadvar.set_env_global() 
          }
          catch (e)
          {
            echo 'No existen variables Globales ' + e.toString()
          }
          loadvar.setenv()
        }
        stage('Lambda Yarn Test')
        {
          devops.lambda_yarn_test()
        }
        stage('Lambda Yarn Install')
        {
          devops.yarn_install_functions()
        }
        stage('Lambda Sonar')
        {
          devops.lambda_sonar()
        }
        if ( fileExists("${SOURCE_cloudformation}/swagger.yaml") )
        {
          stage('Lambda cp to S3')
          {
            devops.swagger_cp_s3()
          }
        }
        stage('Lambda sam package')
        {
          devops.sam_package()
        }
        stage('Lambda sam deploy')
        {
          devops.sam_deploy()
        }
      }
      catch (e) 
      {
        print e.getMessage()
        print 'Err: Build failed with Error: ' + e.toString()
        print "FALLO !!!!!"
        devops.fail()
      }
      finally 
      {
        devops.reporting()
        devops.postfinal()
      }
    }
  }
}
return this;
