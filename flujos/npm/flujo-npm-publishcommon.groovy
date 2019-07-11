#!groovy
def flujo() 
{
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
        stage('Npm install')
        {
          devops.npm_install()
        }
        stage('Npm publish')
        {
          devops.set_npm_nexus_publish()
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
