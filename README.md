Bring-Your-Own-Device
=====================
Bring Your Own Device (BYOD) refers to the policy to allow employees use their personal devices (mobile, laptops, 
tablets etc.) to access the company's proprietary data. Further details of the problem can be found on 
http://en.wikipedia.org/wiki/Bring_your_own_device.

The software is built on the Kerberos Protocol (http://en.wikipedia.org/wiki/Kerberos_(protocol)). It provides part of
solution of the BYOD problem. 

Major entities in the applications are :
<ol>
<li><b>App:</b> The medium through which user can access the service deployed in the network</li>
<li><b>Service:</b> These are resource which the user is trying to access</li>
<li><b>Kerberos Server:</b> Central Authentication service to authenticate app and user before they can be a part of the trusted
network</li>
<li><b>Key Server:</b> Another service which is responsible to manage keys in the network</li>
<li><b>Apache Directory Studio:</b> Manages user account</li> 
</ol>
Major features of the software are:
<ol>
<li>App and User authentication by the Kerberos Server before they can be a part of the trusted network</li>
<li>App and Service mutual authentication</li>
<li>Session Management to track the activities of the App and User, session timeouts</li>
<li>Secure communication between the App and the Service. Data is encrypted using the "Assymmetric Encryption Scheme"</li>
