Java command line for sending sms with Skylinesms
----------------------------------------

.. note::

    You will need a Skyline sms account for getting your api key and secret. Visit www.skylinesms.com to get started.

To Compile
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code:: bash
    javac Skylinesms.java 


To Send SMS
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code:: bash
    
    java Skylinesms <application key> send <number> <message> <from_number>

To Check SMS Delivery
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code:: bash
    
    java Skylinesms <application key> status <message_id>


To Check Account Balance
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code:: bash
    
    java Skylinesms <application key> balance