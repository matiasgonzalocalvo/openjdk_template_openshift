def modules = [:]
pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            //def common = load "function.groovy"
            steps {
                //def rootDir = pwd()
                //def example = load "${rootDir}@script/Example.Groovy "
                //def common = load "function.groovy"
                script {
                    sh "ls -atlrh"
                    modules.common = load "function.groovy"
                    modules.common.aws_config()
                }
            }
        }
        stage('Test Funciones') {
            steps {
                sh """
                    echo "hola mundo"
                """
            }
        }
    }
    post {
        always {
            echo "always"
            //send_slack("always",":squirrel:")
        }
        failure {
            echo "failure"
            //send_slack("failure",":squirrel:")
        }
        unstable {
            echo "unstable"
            //send_slack("unstable",":squirrel:")
        }
    }
}
