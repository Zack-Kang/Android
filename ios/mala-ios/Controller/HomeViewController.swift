//
//  HomeViewController.swift
//  mala-ios
//
//  Created by Elors on 12/18/15.
//  Copyright © 2015 Mala Online. All rights reserved.
//

import UIKit

private let TeacherTableViewCellReusedId = "TeacherTableViewCellReusedId"

class HomeViewController: UIViewController {
    
    // MARK: - Property
    private var condition: ConditionObject?
    private var filterResultDidShow: Bool = false
    
    
    // MARK: - Components
    private lazy var tableView: TeacherTableView = {
        let tableView = TeacherTableView(frame: self.view.frame, style: .Plain)
        tableView.controller = self
        return tableView
    }()
    
    
    // MARK: - Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupNotification()
        setupUserInterface()
        // 开启下拉刷新
        self.tableView.startPullToRefresh() //loadTeachers()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        makeStatusBarBlack()
        filterResultDidShow = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    // MARK: - Private Method
    private func setupNotification() {
        NSNotificationCenter.defaultCenter().addObserverForName(
            MalaNotification_CommitCondition,
            object: nil,
            queue: nil) { [weak self] (notification) -> Void in
                if !(self?.filterResultDidShow ?? false) {
                    self?.filterResultDidShow = true
                    self?.condition = notification.object as? ConditionObject
                    self?.resolveFilterCondition()
                }
        }
    }
    
    private func setupUserInterface() {
        // Style
        self.title = MalaCommonString_Malalaoshi
        // 下拉刷新组件
        self.tableView.addPullToRefresh({ [weak self] in
            self?.loadTeachers()
            })
        
        // SubViews
        self.view.addSubview(tableView)
        
        // Autolayout
        tableView.snp_makeConstraints { (make) -> Void in
            make.top.equalTo(self.view.snp_top)
            make.left.equalTo(self.view.snp_left)
            make.bottom.equalTo(self.view.snp_bottom)
            make.right.equalTo(self.view.snp_right)
        }
        
        // 设置BarButtomItem间隔
        let spacer = UIBarButtonItem(barButtonSystemItem: .FixedSpace, target: nil, action: nil)
        spacer.width = -MalaLayout_Margin_5*2.3
        
        // leftBarButtonItem
        let leftBarButtonItem = UIBarButtonItem(customView:
            UIButton(
                title: "洛阳",
                imageName: "location_normal",
                highlightImageName: "location_press",
                target: self,
                action: "profileButtonDidClick"
            )
        )
        navigationItem.leftBarButtonItems = [spacer, leftBarButtonItem]
        
        // rightBarButtonItem
        let rightBarButtonItem = UIBarButtonItem(customView:
            UIButton(
                imageName: "filter_normal",
                highlightImageName: "filter_press",
                target: self,
                action: "filterButtonDidClick"
            )
        )
        navigationItem.rightBarButtonItems = [spacer, rightBarButtonItem]
    }
    
    private func loadTeachers(filters: [String: AnyObject]? = nil) {
        
        MalaNetworking.sharedTools.loadTeachers(filters) { [weak self] result, error in
            if error != nil {
                debugPrint("HomeViewController - loadTeachers Request Error")
                return
            }
            guard let dict = result as? [String: AnyObject] else {
                debugPrint("HomeViewController - loadTeachers Format Error")
                return
            }
            
            self?.tableView.teachers = []
            let resultModel = ResultModel(dict: dict)
            if resultModel.results != nil {
                for object in ResultModel(dict: dict).results! {
                    if let dict = object as? [String: AnyObject] {
                        self?.tableView.teachers!.append(TeacherModel(dict: dict))
                    }
                }
            }
            self?.tableView.reloadData()
        }
    }
    
    private func resolveFilterCondition() {
        let viewController = FilterResultController()
        viewController.filterCondition = self.condition
        navigationController?.pushViewController(viewController, animated: true)
    }
    
    
    // MARK: - Event Response
    @objc private func locationButtonDidClick() {
        //TODO:定位功能代码
    }

    @objc private func filterButtonDidClick() {
        ThemeAlert(contentView: FilterView(frame: CGRectZero)).show()
    }
    
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self, name: MalaNotification_CommitCondition, object: nil)
    }
    
    @objc private func profileButtonDidClick() {
        _ = JSSAlertView().show(self,
            title: "目前只支持洛阳，其他地区正在拓展中...",
            buttonText: "我知道了",
            iconImage: UIImage(named: "alert_Position")
        )
    }
}