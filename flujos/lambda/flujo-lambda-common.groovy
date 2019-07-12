#!groovy
def flujo() 
{
  /*
    Genero Variables Globales que se van a utilizar
  */
  env.SOURCE_cloudformation="cloudformations"
  env.SOURCE_functions="functions"
  env.SOURCE_scripts="scripts"
  /* 
    Generando un numero ramdon que se va a utilizar en el environment y en la ejecucion del sam
  */
  def verCode = UUID.randomUUID().toString()
  env.random="${verCode}"
  /*
    genero node_docker default
  */
  node_docker="arch-jenkins-slave-front"
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
        /*
        stage('Lambda Yarn Test')
        {
          devops.lambda_yarn_test()
        }*/
        stage('Lambda Yarn Install')
        {
          devops.lambda_yarn_install()
        }
        /*
        stage('Lambda Sonar')
        {
          devops.lambda_sonar()
        }
        */
        if ( fileExists("${SOURCE_cloudformation}/swagger.yaml") )
        {
          stage('Lambda cp to S3')
          {
            devops.swagger_cp_s3()
          }
        }
        stage('Lambda sam package security')
        {
          devops.lambda_sam_package_security()
        }
        stage('Lambda sam deploy security')
        {
          devops.lambda_sam_deploy_security()
        }
        stage('Lambda sam package')
        {
          devops.lambda_sam_package()
        }
        stage('Lambda sam deploy')
        {
          devops.lambda_sam_deploy()
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
