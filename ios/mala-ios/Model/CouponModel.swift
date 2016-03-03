//
//  CouponModel.swift
//  mala-ios
//
//  Created by 王新宇 on 2/19/16.
//  Copyright © 2016 Mala Online. All rights reserved.
//

import UIKit

class CouponModel: NSObject {

    // MARK: - Property
    /// 编号
    var id: Int = 0
    /// 名称
    var name: String = ""
    /// 金额
    var amount: Int = 0
    /// 有效期
    var expired_at: NSTimeInterval = 0
    /// 使用标记
    var used: Bool = true
    
    /// 状态
    var status: CouponStatus?
    
    
    ///  根据[有效期]、[使用标记] 生成状态
    func setupStatus() {
        // 已使用
        if used {
            status = .Used
        // 已过期
        }else if couponIsExpired(expired_at) {
            status = .Expired
        // 未使用
        }else {
            status = .Unused
        }
    }
    
    
    // MARK: - Constructed
    override init() {
        super.init()
    }
    
    init(dict: [String: AnyObject]) {
        super.init()
        setValuesForKeysWithDictionary(dict)
    }
    
    convenience init(id: Int, name: String, amount: Int, expired_at: NSTimeInterval, used: Bool) {
        self.init()
        self.id = id
        self.name = name
        self.amount = amount
        self.expired_at = expired_at
        self.used = used
    }
    
    
    // MARK: - Description
    override var description: String {
        return "CouponModel(id: \(id), name: \(name), amount: \(amount), expired_at: \(String(timeStamp: expired_at)))" +
        ", used: \(used))\n"
    }
}