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
  //node_docker="jenkins-slave-comafi-nodejsdtk"
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
        
        stage('Front ng Test')
        {
          devops.ng_test()
        }
        stage('Front ng Build')
        {
          devops.ng_build()
        }
        /*
        stage('Front Sonar')
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
        stage('Front sam package')
        {
          devops.lambda_sam_package()
        }
        stage('Front sam deplot cp to S3')
        {
          devops.lambda_sam_deploy()
        }
        stage('Front cp to s3')
        {
          devops.front_s3_cp()
        }
        stage('Front cp index to s3 ')
        {
          devops.front_s3_cp_index()
        }
        /*
        stage('Front  ')
        {
          devops.cloudfront_invalidation()
        }
        */
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
