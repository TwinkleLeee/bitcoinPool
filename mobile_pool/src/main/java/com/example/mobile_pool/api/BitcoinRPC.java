package com.example.mobile_pool.api;

public interface BitcoinRPC {

    /**
     * 创建BTC钱包地址(钱包、地址都是新创建)
     * @param walletName 钱包名称
     * @param walletPassword 钱包密码
     * @param addressName 地址显示名称
     * @return 地址
     */
    String createBtcAddress(String walletName,String walletPassword,String addressName);

    /**
     * 获取BTC余额
     * @param walletName 钱包名称
     * @param address 地址
     * @return 余额
     */
    double getBtcBalance(String walletName,String address);

    /**
     * BTC转账
     * @param walletName 钱包名称
     * @param walletPassword 钱包密码
     * @param toAddress 接收地址
     * @param quantity 转账数量
     * @return 转账状态消息
     */

    /**
     * 获取地址私钥
     * @param walletName 钱包名称
     * @param walletPassword 钱包密码
     * @param address 地址
     * @return 私钥
     */
    String getBtcPrivateKey(String walletName,String walletPassword,String address);

    /**
     * 导入BTC私钥(导入地址到钱包)
     * @param walletName 钱包名称
     * @param walletPassword 钱包密码
     * @param privateKey 私钥
     * @return 是否成功
     */
    boolean importBtcPrivateKey(String walletName,String walletPassword,String privateKey);
}
