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
                    for i in $(seq 1 10) ; do
                        echo $i
                        sleep 1 
                    done 
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
