Bring-Your-Own-Device
=====================
Bring Your Own Device (BYOD) refers to the policy to allow employees use their personal devices (mobile, laptops, 
tablets etc.) to access the company's proprietary data. Further details of the problem can be found on 
http://en.wikipedia.org/wiki/Bring_your_own_device.

The software is built on the Kerberos Protocol (http://en.wikipedia.org/wiki/Kerberos_(protocol)). It provides part of
solution of the BYOD problem. 

Major entities in the applications are :
1) App: The medium through which user can access the service deployed in the network
2) Service: These are resource which the user is trying to access
3) Kerberos Server: Central Authentication service to authenticate app and user before they can be a part of the trusted
network
4) Key Server: Another service which is responsible to manage keys in the network
5) Apache Directory Studio: Manages user account 

Major features of the software are:
1) App and User authentication by the Kerberos Server before they can be a part of the trusted network
2) App and Service mutual authentication
3) Session Management to track the activities of the App and User, session timeouts 
4) Secure communication between the App and the Service. Data is encrypted using the "Assymmetric Encryption Scheme"
