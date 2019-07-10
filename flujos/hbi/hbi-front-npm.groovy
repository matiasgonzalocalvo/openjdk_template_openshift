#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      //def cuentas=["AWS_DESA_CMF", "AWS_DESA","_srv_jenkins_pec"]
      stage('Set ENV')
      {
        loadvar.set_env_global()
        loadvar.setenv()
        devops.set_npm_nexus()
      }
      /*stage('test') 
      {
        devops.test_npm_comafi_digital()
      }
      stage('SonarQube analysis') 
      {
        devops.sonar_js("${sonar_projectKey}", "${sonar_exclusions}", "${sonar_javascript_lcov_reportPaths}")
      }
      stage("Quality Gate")
      {
        sh "echo sonar "
        //devops.wait_sonar()
      }*/
      stage("npm install")
      {
        //devops.build_comafi_digital()
        devops.npm_install()
      }
      stage("install comafi common")
      {
        devops.npm_install_comafi_common()
      }
      stage("npm run build")
      { 
        devops.npm_run_build_env()
      }
      stage("Deploy S3")
      {
        devops.upload_s3()
      }
    }
    catch (e) 
    {
      echo e.getMessage()
      echo 'Err: Build failed with Error: ' + e.toString()
      echo "FALLO !!!!!"
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
return this;
