node {
    def app

    stage('Clone repository') {
        checkout scm
    }

    stage('Build image') {     
        app = docker.build("jobsee")
    }
    
    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        docker.withRegistry('https://192.168.1.8/v2/', 'f1ea73ba-b508-4e36-b7cc-786978e59829') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
    }
}
