# CS 305 Software Security Portfolio

### Artifact

---

📄 Artemis Financial Practices for Secure Software Report (Project Two)

[CS 305 Artemis Financial Practices for Secure Software Report](./CS_305_Artemis_Financial_Practices_for_Secure_Software.pdf)

### Project Overview

---

Artemis Financial is a consulting company that develops personalized financial planning solutions for clients, including savings, retirement, investment, and insurance planning. The company requested improvements to the security of its public web application to better protect sensitive client data during transmission and processing.

The primary issue addressed in this project was strengthening secure communications and verifying data integrity. The goal was to refactor the existing application to implement checksum verification, enable HTTPS communication, and follow modern secure coding practices to reduce software vulnerabilities.


### Identifying Security Vulnerabilities

---

One area I performed well in was analyzing the application’s dependencies and configuration to identify potential security risks. Using static analysis tools helped reveal outdated libraries and possible vulnerabilities that could expose sensitive data.

Secure coding is important because financial applications manage confidential information that must remain accurate and protected. Strong software security reduces the risk of data breaches, protects customer trust, and helps organizations avoid financial and reputational damage.

### Challenges and Learning Experience

---

The most challenging part of the vulnerability assessment was understanding how reported vulnerabilities related to actual runtime risk. Some findings were false positives, which required manual investigation and research to confirm whether they affected the application.

This process was also one of the most helpful learning experiences because it demonstrated that security scanning tools assist decision-making but still require developer analysis and judgment.

### Increasing Layers of Security

---

Security was improved by implementing multiple defensive layers rather than relying on a single solution. These included:

* Implementing SHA-256 checksum verification to validate data integrity
* Enabling HTTPS using TLS certificates for encrypted communication
* Adding input validation and output encoding to prevent malicious input
* Updating dependencies and frameworks to supported versions

In future projects, I would continue using automated vulnerability scanners, dependency analysis tools, and secure design principles to evaluate risks and determine appropriate mitigation strategies.

### Ensuring Functionality and Security

---

After refactoring the application, I verified both functionality and security through multiple testing approaches:

* Manual functional testing to confirm application behavior
* HTTPS verification through secure browser connections
* Checksum validation testing using unique input data
* Secondary static testing using OWASP Dependency-Check

Running dependency scans after refactoring ensured that new vulnerabilities were not introduced during development.

### Tools, Resources, and Practices Used

---

This project introduced several tools and practices that will be valuable in future development work:

* OWASP Dependency-Check for vulnerability scanning
* Java Keytool for certificate generation
* Spring Boot secure configuration practices
* Java MessageDigest for cryptographic hashing
* Secure input validation and output encoding techniques

These tools reinforced the importance of integrating security throughout the development lifecycle rather than treating it as a final step.

### Repository Contents

---

* Secure Software Practices Report (PDF)
* Refactored Spring Boot application code
* Security configuration updates
* Supporting documentation and screenshots

### Additional Artifacts

---

In addition to the secure software implementation report, this repository also includes the Artemis Financial Vulnerability Assessment Report (Project One).

📄 Vulnerability Assessment Report (PDF)

[CS 305 Artemis Financial Vulnerability Assessment Report](./CS_305_Artemis_Vulnerability_Report.pdf)

This report covers the first security review of the Artemis Financial application before any refactoring started. The review found possible risks with dependencies, insecure ways of sharing information, and places where data integrity needed better protection.

Project Two shows how secure coding practices and technical fixes were put in place. Project One focuses on how vulnerabilities were found and which risks were addressed first. Together, these projects show the full process of secure software development, from finding problems to fixing and checking them.