pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            steps {
                def common = load "function.groovy"
                aws_config()
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
