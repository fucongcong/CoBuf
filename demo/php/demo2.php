<?php 

// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
// }
// 

class Company {
    public $id;
    public $code;
    public $name;
    public $addr;

    public function __construct()
    {
        $this->id = new CoInt;
        $this->code = new CoLong;
        $this->name = new CoString;
        $this->addr = new CoString;
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

    public function pack(&$val)
    {
        $this->id->get($val, 0, true);
        $this->code->get($val, 1, true);
        $this->name->get($val, 2, true);
        $this->addr->get($val, 16, false);
    }

    public function unpack($data)
    {
        $this->id = $this->id->set($data, 0);
        $this->code = $this->code->set($data, 1);
        $this->name = $this->name->set($data, 2);
        $this->addr = $this->addr->set($data, 16);
    }
}

$data = null;
$obj = new Company;
$obj->setId(10);
$obj->setCode(1111);
$obj->setName('coco');
$obj->setAddr('aaaaaaaaaa');
$obj->pack($data);
file_put_contents('1', $data);
echo "压缩后总长度为".strlen($data)."\n";

$unpackData = coUnpack($data);
$obj = new Company;
$obj->unpack($unpackData);
print_r($unpackData);
print_r($obj);


//lib
function coUnpack($data)
{
    $len = 0;
    $unpackData = [];
    while (strlen($data) - $len > 0) {

        if ($len > 0) $data = substr($data, $len);
        $typeAndTag = ord(substr($data, 0, 1));
        $type = $typeAndTag >> 4;

        $tag = $typeAndTag - ($type << 4);
        if ($tag == 15) {
            $data = substr($data, 1);
            $tag = ord(substr($data, 0, 1));
        }

        $data = substr($data, 1);
        if ($type == CoType::CO_INT) {
            $len = 4;
            $val = unpack("N", substr($data, 0, 4));
            $val = $val[1];
        } elseif ($type == CoType::CO_LONG) {
            $len = 8;
            $val = unpack("J", substr($data, 0, 8));
            $val = $val[1];
        } else  {
            $len = unpack("N", substr($data, 0, 4));
            $len = $len[1];

            $data = substr($data, 4);
            $val = unpack("a*", substr($data, 0, $len));
            $val = $val[1];
        }

        $unpackData[$tag] = [
            'type' => $type,
            'tag' => $tag,
            'len' => $len,
            'val' => $val,
        ];
    }

    return $unpackData;
}

function coPack($data, $type, $tag, &$val)
{
    if ($tag >= 15) {
        $val .= chr(($type << 4) | 15);
        $val .= chr($tag).$data;
    } else {
        $val .= chr(($type << 4) | $tag).$data;
    }
}

class CoType
{
    const CO_INT = 1;
    const CO_LONG = 2;
    const CO_STRING = 3;
}

class DataType 
{   
    public function packVal(&$val, $type, $tag, $isNeed = true)
    {   
        if (!isset($this->val)) {
            $this->val = null;
        }

        if ($this->val != null) {
            coPack($this->val, $type, $tag, $val);
        } else {
            if ($isNeed) {
                throw new Exception("缺少必要的参数标记为:{$tag}", 1);
            }
        }
    }

    public function setVal($data)
    {
        $this->val = pack("a*", $data);
        $bodyLen = strlen($this->val);
        $head = pack("N", $bodyLen);
        $this->val = $head.$this->val;
        return $this->val;
    }
}

class CoInt extends DataType
{   
    public function setVal($data)
    {   
        $this->val = pack("N", intval($data));
        return $this->val;
    }

    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_INT, $tag, $isNeed);
    }

    public function set($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_INT) {
            return $data[$tag]['val'];
        }
    }
}

class CoLong extends DataType
{   
    public function setVal($data)
    {   
        $this->val = pack("J", intval($data));
        return $this->val;
    }
    
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_LONG, $tag, $isNeed);
    }

    public function set($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_LONG) {
            return $data[$tag]['val'];
        }
    }
}

class CoString extends DataType
{   
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_STRING, $tag, $isNeed);
    }

    public function set($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_STRING) {
            return $data[$tag]['val'];
        }
    }
}