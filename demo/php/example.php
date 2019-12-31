<?php 

require __DIR__.'/lib/Co.php';

// struct Members
// {
//     0 need list<string> names;
//     1 need map<string, string> sexs;
//     2 need optional bool work;
// }
// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
//     4 optional Members members;
//     5 optional list<Members> memberLists;
//     6 optional map<string, Members> memberMaps;
// }
// 
// struct User
// {
//     0 need int id;
//     1 optional Company company;
//     2 optional bool working;
// }

class Members extends CoStruct {
    public $names;
    public $sexs;
    public $work;

    public function __clone()
    {
        $this->names = clone $this->names;
    }

    public function __construct()
    {
        $this->names = new CoList(new CoString);
        $this->sexs = new CoMap(new CoString, new CoString);
        $this->work = new CoBool;
    }

    public function setNames($names)
    {
        $this->names->setVal($names);
    }

    public function setSexs($sexs)
    {
        $this->sexs->setVal($sexs);
    }

    public function setWork($work)
    {
        $this->work->setVal($work);
    }

    public function pack(&$val)
    {
        $this->names->get($val, 0, true);
        $this->sexs->get($val, 1, false);
        $this->work->get($val, 2, false);
    }

    public function unpack($data)
    {
        $this->names = $this->names->getUnpackData($data, 0);
        $this->sexs = $this->sexs->getUnpackData($data, 1);
        $this->work = $this->work->getUnpackData($data, 2);
    }
}

class Company extends CoStruct {

    public $id;

    public $code;

    public $name;

    public $addr;

    public $members;

    public $memberLists;

    public $memberMaps;

    public function __clone()
    {
        $this->id = clone $this->id;
        $this->code = clone $this->code;
        $this->name = clone $this->name;
        $this->addr = clone $this->addr;
        $this->members = clone $this->members;
        $this->memberLists = clone $this->memberLists;
        $this->memberMaps = clone $this->memberMaps;
    }

    public function __construct()
    {
        $this->id = new CoInt;
        $this->code = new CoLong;
        $this->name = new CoString;
        $this->addr = new CoString;
        $this->members = new Members;
        $this->memberLists = new CoList(new Members);
        $this->memberMaps = new CoMap(new CoString, new Members);
    }

    public function setId($id)
    {
        $this->id->setVal($id);
    }

    public function setCode($code)
    {
        $this->code->setVal($code);
    }

    public function setName($name)
    {
        $this->name->setVal($name);
    }

    public function setAddr($addr)
    {
        $this->addr->setVal($addr);
    }

    public function setMembers($members)
    {
        $this->members->setVal($members);
    }

    public function setMemberLists($memberLists)
    {
        $this->memberLists->setVal($memberLists);
    }

    public function setMemberMaps($memberMaps)
    {
        $this->memberMaps->setVal($memberMaps);
    }

    public function pack(&$val)
    {
        $this->id->get($val, 0, true);
        $this->code->get($val, 1, true);
        $this->name->get($val, 2, true);
        $this->addr->get($val, 16, false);
        $this->members->get($val, 4, false);
        $this->memberLists->get($val, 5, false);
        $this->memberMaps->get($val, 6, false);
    }

    public function unpack($data)
    {   
        $this->id = $this->id->getUnpackData($data, 0);
        $this->code = $this->code->getUnpackData($data, 1);
        $this->name = $this->name->getUnpackData($data, 2);
        $this->addr = $this->addr->getUnpackData($data, 16);
        $this->members = $this->members->getUnpackData($data, new Members, 4);
        $this->memberLists = $this->memberLists->getUnpackData($data, 5);
        $this->memberMaps = $this->memberMaps->getUnpackData($data, 6);
    }
}

class User extends CoStruct {
    public $id;
    public $company;
    public $working;

    public function __clone()
    {
        $this->id = clone $this->id;
        $this->company = clone $this->company;
        $this->working = clone $this->working;
    }

    public function __construct()
    {
        $this->id = new CoInt;
        $this->company = new Company;
        $this->working = new CoBool;
    }

    public function setId($id)
    {
        $this->id->setVal($id);
    }

    public function setCompany($company)
    {
        $this->company->setVal($company);
    }

    public function setWorking($working)
    {
        $this->working->setVal($working);
    }

    public function pack(&$val)
    {
        $this->id->get($val, 0, true);
        $this->company->get($val, 1, false);
        $this->working->get($val, 2, false);
    }

    public function unpack($data)
    {   
        $this->id = $this->id->getUnpackData($data, 0);
        $this->company = $this->company->getUnpackData($data, new Company, 1);
        $this->working = $this->working->getUnpackData($data, 2);
    }
}


$data = null;

$members = new Members;
$members->setNames(["aa", "cc", "dd"]);
$members->setSexs([
    "aa" => "男",
    "cc" => "女"
]);

$members2 = new Members;
$members2->setNames(["coco"]);
$members2->setWork(true);

$company = new Company;
$company->setId(10);
$company->setCode(1111);
$company->setName('coco');
$company->setAddr('aaaaaaaaaa');
$company->setMembers($members);
$company->setMemberLists([$members, $members2]);
$company->setMemberMaps(['test1' => $members, 'test2' => $members2]);

$user = new User;
$user->setId(1);
$user->setCompany($company);
$user->setWorking(true);
$user->pack($data);

$packLen = strlen($data);

$unpackData = coUnpack($data);
$obj = new User;
$obj->unpack($unpackData);
print_r($obj);//var_dump($obj)

