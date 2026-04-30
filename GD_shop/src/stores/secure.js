import SecureLS from 'secure-ls'
const ls = new SecureLS({
    isCompression: false, // 作用是压缩数据，减少存储空间
    encryptionSecret: 'chen-zhicong-web', // 自定义密钥
    encodingType: 'aes', // 加密算法：aes / des / rabbit / rc4 等
    storage:sessionStorage // 关键：使用 sessionStorage
})
const SelfStorage = {
    setItem(key, value) {
        ls.set(key, value)
    },
    getItem(key) {
        return ls.get(key)
    }
}
export default SelfStorage