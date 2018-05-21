package proj.abigo.coco.cocoapplication.Bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import proj.abigo.coco.cocoapplication.BluetoothActivity;
import proj.abigo.coco.cocoapplication.LoginActivity;
import proj.abigo.coco.cocoapplication.MyFeed.MyFeedFragment;


/**
 * Created by DS on 2018-03-23.
 */

public class BluetoothService {

    private static final String TAG = "BluetoothService";

    // 블루투스 통신 프로토콜
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // 상태를 나타내는 상태 변수
    public static final int STATE_NONE = 0; // 아무런 일 X
    public static final int STATE_LISTEN = 1; // 연결을 위한 리스닝 상태
    public static final int STATE_CONNECTING = 2; // 연결 과정 시작
    public static final int STATE_CONNECTED = 3; // 연결된 상태
    public static final int STATE_FAIL = 4; // 연결 실패

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter btAdapter;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private Activity mActivity;
    private Handler mHandler;

    private int mState;

    /* BluetoothService 생성자*/
    public BluetoothService(Activity activity, Handler handler) {
        mActivity = activity;
        mHandler = handler;

        // BluetoothAdapter 얻기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /* 블루투스 지원 기기 확인*/
    public boolean getDeviceState(){
        Log.i(TAG, "Bluetooth check");

        if(btAdapter == null){
            Log.d(TAG,"Bluetooth is not available");
            return false;
        }
        else{
            Log.d(TAG, "Bluetooth is available");
            return true;
        }
    }

    /* 블루투스 상태 체크 */
    public void enableBluetooth(){
        Log.i(TAG, "Check the enable Bluetooth");

        if(btAdapter.isEnabled()){
            // 블루투스 상태가 ON인 경우
            Log.d(TAG, "Bluetooth Enable Now");
            scanDevice();

        } else{
            // 블루투스 상태가 OFF인 경우
            Log.d(TAG, "Bluetooth Enable Request: ");

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }

    /* 기기 접속 요청*/
    public void scanDevice(){
        Log.d(TAG, "Scan Device");

        Intent intent = new Intent(mActivity, DeviceListActivity.class);
        mActivity.startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
    }

    /* 검색된 기기에 접속 */
    public void getDeviceInfo(Intent data){
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        Log.d(TAG, "Get Device address :" + address);

        connect(device);
    }

    /* 블루투스 상태 set */
    private synchronized void setState(int state){
        Log.d(TAG, "setState() " + mState + "-> "+ state);
        mState = state;

        // 블루투스 상태를 handler 를 통해 넘겨줌
        mHandler.obtainMessage(BluetoothActivity.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /* 블루투스 상태 get */
    public synchronized int getState(){
        return mState;
    }

    public synchronized void start(){
        Log.d(TAG, "start");

        if(mConnectThread == null){}
        else{
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread == null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    /* ConnectThread 초기화 _ device의 모든 연결 제거*/
    public synchronized  void connect(BluetoothDevice device){

        Log.d(TAG, "connnect to : " + device);

        if(mState == STATE_CONNECTING){
            if(mConnectThread == null){}
            else{
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        if(mConnectedThread == null){}
        else{
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectThread = new ConnectThread(device);

        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /* ConnectedThread 초기화 */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device){

        Log.d(TAG, "connected");

        if(mConnectThread == null){}
        else{
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread == null){}
        else{
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectedThread = new ConnectedThread(socket);

        mConnectedThread.start();
        setState(STATE_CONNECTED);

    }

    /* 모든 thread stop*/
    public synchronized void stop(){

        Log.d(TAG, "stop");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    /* 값을 보냄*/
    public void write(byte[] out){
        ConnectedThread r;
        synchronized (this){
            if(mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        }
    }

    /* 연결에 실패했을 때*/
    private void connectionFailed(){
        setState(STATE_FAIL);
    }

    /* 연결을 잃었을 때 */
    private void connectionLost(){
        setState(STATE_LISTEN);
    }


    /* socket, thread 생성 */
    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null; // 디바이스 정보를 얻어서 BluetoothSocket 생성
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }

            mmSocket = tmp;
        }

        public void run() {

            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // 연결을 시도하기 전에는 항상 기기 검색을 중지한다.
            // 기기 검색이 계속되면 연결속도가 느려지기 때문이다.
            btAdapter.cancelDiscovery();

            // BluetoothSocket 연결 시도
            try {

                // BluetoothSocket 연결 시도에 대한 return 값은 succes 또는 exception이다.
                mmSocket.connect();
                Log.d(TAG, "Connect Success");

            } catch (IOException e) {

                connectionFailed();    // 연결 실패시 불러오는 메소드
                Log.d(TAG, "Connect Fail");

                // socket을 닫는다.
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // 연결중? 혹은 연결 대기상태인 메소드를 호출한다.
                BluetoothService.this.start();
                return;
            }

            // ConnectThread 클래스를 reset한다.
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // ConnectThread를 시작한다.
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread{

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            Log.d(TAG, "create ConnectedThread");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");

            byte[] readBuffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    int bytesAvailable = mmInStream.available(); // 수신 데이터 확인
                    if(bytesAvailable > 0){
                        // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                        bytes = mmInStream.read(readBuffer);
                        final String readingMessage = new String(readBuffer, "US-ASCII");

                        mHandler.obtainMessage(MyFeedFragment.MESSAGE_READING, bytes, -1, readingMessage).sendToTarget();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /** * Write to the connected OutStream. * @param buffer The bytes to write */
        public void write(byte[] buffer) {
            try {
                // 값을 쓰는 부분(값을 보낸다)
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mConnectedThread.interrupt();
                mmSocket.close();
                mmInStream.close();
                mmOutStream.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

    }

}
