import 'package:flutter/material.dart';
import 'package:flutter/services.dart' as fsv;
import 'dart:typed_data';
import 'dart:convert';
import 'dart:async';

void main() {
  runApp(new MyApp());

}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    fsv.PlatformMessages.setJSONMessageHandler("hi",(String v) async{
      return v;
    });
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String message = "";

   _incrementCounter() async {
    //
     String buffer1 = await fsv.PlatformMessages.sendString("beacon.requestPermission", JSON.encode({"test":"hello"}));
     String buffer2 = "-";
     if(buffer1 == """{"r":"ok"}""")
     {
       buffer2 = await fsv.PlatformMessages.sendString("beacon.startLescan", JSON.encode({"test":"hello"}));
     }


     message = ":: ${buffer1} :: ${buffer2}";
     setState(() {});
     await new Future.delayed(new Duration(seconds: 2));
     String buffer4 = await fsv.PlatformMessages.sendString("beacon.startAdvertiseBeacon", JSON.encode({"test":"hello"}));
     setState(() {});

     await new Future.delayed(new Duration(seconds: 5));
     String buffer3 = await fsv.PlatformMessages.sendString("beacon.getFoundBeacon", JSON.encode({"test":"hello"}));
     message = buffer3 + buffer4;
     setState(() {});


    //

  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(config.title),
      ),
      body: new Center(
        child: new Text(
          '${message} $_counter time${ _counter == 1 ? '' : 's' }.',
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: new Icon(Icons.add),
      ), // This trailing comma tells the Dart formatter to use
    );
  }
}
