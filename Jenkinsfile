def component = [
		seboard: true,
		nginx: true
]

pipeline {
	agent any
	stages {
		stage("Checkout") {
			steps {
				checkout scm
			}
		}
		stage("Build") {
			steps {
                script {
			
			sh "cp /var/jenkins_home/workspace/seboard-pipeline/application.yml /var/jenkins_home/workspace/seboard-pipeline/seboard/src/main/resources"
			sh "cp /var/jenkins_home/workspace/seboard-pipeline/application-security.yml /var/jenkins_home/workspace/seboard-pipeline/seboard/src/main/resources"
			sh "cp /var/jenkins_home/workspace/seboard-pipeline/application-db.yml /var/jenkins_home/workspace/seboard-pipeline/seboard/src/main/resources"
					component.each{ entry ->
						stage ("${entry.key} Build"){
							if(entry.value){
								var = entry.key
								sh "docker build -t maanjong/se-dev:${entry.key} ${entry.key}"
							}	
						}
					}
				}
			}
		}
		stage("Tag and Push") {
			steps {
                script {
					component.each{ entry ->
						stage ("${entry.key} Push"){
							if(entry.value){
								var = entry.key
// 								withCredentials([[$class: 'UsernamePasswordMultiBinding',
// 								credentialsId: 'maanjong-docker-hub',
// 								usernameVariable: 'DOCKER_USER_ID',
// 								passwordVariable: 'DOCKER_USER_PASSWORD'
// 								]]){
// 								sh "echo ${DOCKER_USER_ID} ${DOCKER_USER_PASSWORD}"
// 								sh "docker login --username=${DOCKER_USER_ID} --password=${DOCKER_USER_PASSWORD}"
								sh "docker push maanjong/se-dev:${entry.key}"
//								}
							}
						}
					}
				}
			}	
		}
		stage("Execute") {
            steps {
                sh "docker compose up -d"
            }
        }
	}

}
