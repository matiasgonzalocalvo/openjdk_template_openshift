#!groovy
def flujo() 
{
  try
  {
    loadvar.set_env_global()
    print node_docker 
  }
  catch (e)
  {
    print e.getMessage()
    print 'Err:  ' + e.toString()
    echo "node_docker no existe seteo jenkins-slave-comafi-nodejsdtk"
    //node_docker="jenkins-slave-comafi-nodejsdtk"
    node_docker="arch-jenkins-slave-front"
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
          //devops.set_scripts_comafi_digital()
          //devops.docker_login()
        }
        stage("Build SURF SAM FrontEnd Project")
        {
          devops.new_process_sam()
          devops.ng_build()
        }
        stage("Deploy SURF SAM FrontEnd Project")
        {
          devops.sam_deploy()
          devops.upload_s3()
          devops.cloudfront_invalidation()
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
        stage('Reportes')
        {
          devops.reporting()
          devops.postfinal()
        }
      }
    }
  }
}
return this;
