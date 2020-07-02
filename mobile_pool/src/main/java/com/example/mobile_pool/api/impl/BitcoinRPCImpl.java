package com.example.mobile_pool.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mobile_pool.api.BitcoinRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BitcoinRPCImpl implements BitcoinRPC {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String user="";//lpz
    private String password="";//lpz
    private String port="";//18332 因为测试网是18332  主网是8332
    private String host="";//127.0.0.1
    private final int WALLET_PASS_PHRASE_TIME_OUT=60;

    @Override
    public String createBtcAddress(String walletName, String walletPassword, String addressName) {
        try{
            //获取BTC客户端默认钱包
            BitcoinJSONRPCClient bitcoinClient=getBitcoinClient("");
            //创建钱包
            String walletResult=bitcoinClient.query("createwallet",walletName).toString();
            logger.info("钱包("+walletName+")创建成功返回==>"+walletResult+"");
            //获取当前BTC客户端钱包
            bitcoinClient=getBitcoinClient(walletName);
            //设置钱包密码
            bitcoinClient.encryptWallet(walletPassword);
            //创建地址
            return bitcoinClient.getNewAddress(addressName);
        }catch (Exception e){
            logger.error("创建钱包地址异常==>"+e.getMessage());
        }
        return null;
    }

    @Override
    public double getBtcBalance(String walletName, String address) {
        try {
            List<JSONArray> jsonArrayList=getListAddressInfo(walletName);
            for(JSONArray jsonArray:jsonArrayList){
                if(jsonArray.getString(0).equals(address)){
                    return jsonArray.getDouble(1);
                }
            }
        }catch (Exception e){
            logger.error("钱包("+walletName+")地址("+address+")获取余额异常==>"+e.getMessage());
        }
        return 0;
    }



    @Override
    public String getBtcPrivateKey(String walletName, String walletPassword, String address) {
        try {
            BitcoinJSONRPCClient bitcoinClient=getBitcoinClient(walletName);
            bitcoinClient.walletPassPhrase(walletPassword,WALLET_PASS_PHRASE_TIME_OUT);
            Object addressPrivate=bitcoinClient.query("dumpprivkey",address);
            return addressPrivate.toString();
        }catch (Exception e){
            logger.error("获取地址("+address+")私钥异常==>"+e.getMessage());
        }
        return null;
    }

    @Override
    public boolean importBtcPrivateKey(String walletName, String walletPassword, String privateKey) {
        try {
            BitcoinJSONRPCClient bitcoinClient=getBitcoinClient(walletName);
            bitcoinClient.walletPassPhrase(walletPassword,WALLET_PASS_PHRASE_TIME_OUT);
            bitcoinClient.importPrivKey(privateKey);
            return true;
        }catch (Exception e){
            logger.error("导入地址私钥("+privateKey.substring(10)+"******)到钱包("+walletName+")异常==>"+e.getMessage());
        }
        return false;
    }

    /**
     * 获取BTC客户端
     * @param walletName 钱包名称
     * @return BTC客户端
     */
    private BitcoinJSONRPCClient getBitcoinClient(String walletName){
        BitcoinJSONRPCClient bitcoinClient=null;
        try {
            URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/wallet/"+walletName+"");
            bitcoinClient = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException e) {
            logger.error("获取BTC RPC错误==>"+e.getMessage());
        }
        return bitcoinClient;
    }
    /**
     * 获取所有钱包地址信息
     * @param walletName 钱包名称
     * @return 所有地址信息
     */
    private  List<JSONArray> getListAddressInfo(String walletName){
        BitcoinJSONRPCClient bitcoinClient=getBitcoinClient(walletName);
        List<JSONArray> resultList=new ArrayList<JSONArray>();
        try {
            Object walletAddressAll=bitcoinClient.query("listaddressgroupings");
            JSONArray jsonArrayAll= JSON.parseArray(JSON.toJSONString(walletAddressAll));
            for(int i=0;i<jsonArrayAll.size();i++){
                JSONArray walletArray=JSON.parseArray(jsonArrayAll.get(i).toString());
                for(int j=0;j<walletArray.size();j++){
                    JSONArray jsonArray= JSON.parseArray(walletArray.getString(j));
                    resultList.add(jsonArray);
                }
            }
        }catch (Exception e){
            logger.error("钱包("+walletName+")获取所有地址信息异常==>"+e.getMessage());
        }
        return resultList;
    }
}
