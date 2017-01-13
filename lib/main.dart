import 'package:flutter/material.dart';
import 'package:flutter/services.dart' as fsv;
import 'dart:typed_data';
import 'dart:convert';

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
    ByteData buffer0 = await fsv.PlatformMessages.sendBinary("callback_sync", new ByteData.view(
      new Uint8List.fromList([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]).buffer
    ));

    //
    String buffer1 = await fsv.PlatformMessages.sendString("callback_async", JSON.encode({"test":"hello"}));

    //
    String buffer2 = await fsv.PlatformMessages.sendString("callback_proc", "hello");


    //
    message = "${buffer0.buffer.asUint8List()} :: ${buffer1} :: ${buffer2}" ;
    setState(() {
      _counter++;
    });
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
