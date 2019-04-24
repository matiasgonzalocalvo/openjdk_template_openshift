pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            constants = import("constants.groovy")
            //def common = load "function.groovy"
            steps {
                sh "ls -atlrh"
                //def rootDir = pwd()
                //def example = load "${rootDir}@script/Example.Groovy "
                //def common = load "function.groovy"
                constants.aws_config()
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
            send_slack("always",":squirrel:")
        }
        failure {
            send_slack("failure",":squirrel:")
        }
        unstable {
            send_slack("unstable",":squirrel:")
        }
    }
}
