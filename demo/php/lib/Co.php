<?php 

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
        if ($type == CoType::CO_BOOL) {
            $len = 1;
            $val = ord(substr($data, 0, 1));
        } elseif ($type == CoType::CO_INT) {
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
    const CO_STRUCT = 4;
    const CO_BOOL = 5;
    const CO_LIST = 6;
    const CO_MAP = 7;
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

    public function getListVal($data)
    {
        $len = 0;
        $ret = [];
        while (strlen($data) - $len > 0) {

            if ($len > 0) $data = substr($data, $len);
            $len = unpack("N", substr($data, 0, 4));
            $len = $len[1];

            $data = substr($data, 4);
            $val = unpack("a*", substr($data, 0, $len));
            $val = $val[1];

            $ret[] = $val;
        }

        return $ret;
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

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_INT) {
            return $data[$tag]['val'];
        }
    }

    public function getListVal($data)
    {
        $len = 0;
        $ret = [];
        while (strlen($data) - $len > 0) {

            if ($len > 0) $data = substr($data, $len);
            $len = 4;
            $val = unpack("N", substr($data, 0, $len));
            $val = $val[1];

            $ret[] = $val;
        }

        return $ret;
    }

    public function getCoType()
    {
        return CoType::CO_INT;
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

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_LONG) {
            return $data[$tag]['val'];
        }
    }

    public function getListVal($data)
    {
        $len = 0;
        $ret = [];
        while (strlen($data) - $len > 0) {

            if ($len > 0) $data = substr($data, $len);
            $len = 8;
            $val = unpack("J", substr($data, 0, $len));
            $val = $val[1];

            $ret[] = $val;
        }

        return $ret;
    }

    public function getCoType()
    {
        return CoType::CO_LONG;
    }
}

class CoString extends DataType
{   
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_STRING, $tag, $isNeed);
    }

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_STRING) {
            return $data[$tag]['val'];
        }
    }

    public function getCoType()
    {
        return CoType::CO_STRING;
    }
}

class CoBool extends DataType
{   
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_BOOL, $tag, $isNeed);
    }

    public function setVal($data)
    {   
        if (!is_bool($data)) {
            throw new Exception("CO_BOOL类型不匹配:{$data}", 1);
        }

        $this->val = chr(intval($data));
        return $this->val;
    }

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_BOOL) {
            return boolval($data[$tag]['val']) ? true : false;
        }
    }

    public function getListVal($data)
    {
        $len = 0;
        $ret = [];
        while (strlen($data) - $len > 0) {

            if ($len > 0) $data = substr($data, $len);
            $val = ord(substr($data, 0, 1));
            $len = 1;

            $ret[] = $val;
        }

        return $ret;
    }

    public function getCoType()
    {
        return CoType::CO_BOOL;
    }
}

class CoList extends DataType
{   
    public function __construct($type)
    {
        $this->type = $type;
    }
    
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_LIST, $tag, $isNeed);
    }

    public function setVal($data)
    {   
        if (!is_array($data)) {
            throw new Exception("CO_LIST类型不匹配:{$data}", 1);
        }

        $this->val = null;

        foreach ($data as $value) {
            $struct = clone $this->type;
            $this->val .= $struct->setVal($value);
        }

        $bodyLen = strlen($this->val);
        $head = pack("N", $bodyLen);
        $this->val = $head.$this->val;
        return $this->val;
    }

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_LIST) {
            if (is_subclass_of($this->type, 'CoStruct')) {
                $ret = [];
                $one = $this->type->getListVal($data[$tag]['val']);
                //对每个结构体拆包
                foreach ($one as $value) {
                    $struct = clone $this->type;
                    $unpackData = coUnpack($value);
                    $struct->unpack($unpackData);
                    $ret[] = $struct;
                }

                return $ret;
            } else {
                return $this->type->getListVal($data[$tag]['val']);
            }
        }
    }

    public function getCoType()
    {
        return CoType::CO_LIST;
    }
}

class CoMap extends DataType
{   
    public function __construct($key, $value)
    {
        $this->key = $key;
        $this->value = $value;
    }
    
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_MAP, $tag, $isNeed);
    }

    public function setVal($data)
    {   
        if (!is_array($data)) {
            throw new Exception("CO_MAP类型不匹配:{$data}", 1);
        }

        $this->val = null;

        foreach ($data as $k => $v) {
            $key = clone $this->key;
            $value = clone $this->value;
            $this->val .= chr($key->getCoType()).$key->setVal($k);
            $this->val .= chr($value->getCoType()).$value->setVal($v);
        }

        $bodyLen = strlen($this->val);
        $head = pack("N", $bodyLen);
        $this->val = $head.$this->val;
        return $this->val;
    }

    public function getUnpackData($data, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_MAP) {
            //解包
            $data = $data[$tag]['val'];
            $len = 0;
            $unpackData = [];
            while (strlen($data) - $len > 0) {
                if ($len > 0) $data = substr($data, $len);
                $type = ord(substr($data, 0, 1));
                $data = substr($data, 1);
                if ($type == CoType::CO_BOOL) {
                    $len = 1;
                    $val = ord(substr($data, 0, 1));
                } else {
                    $len = unpack("N", substr($data, 0, 4));
                    $len = $len[1];

                    $data = substr($data, 4);
                    $val = unpack("a*", substr($data, 0, $len));

                    $val = $val[1];
                }

                $unpackData[] = [
                    'type' => $type,
                    'len' => $len,
                    'val' => $val,
                    'tag' => 0,
                ];
            }

            for ($i=0; $i < count($unpackData); $i = $i + 2) {
                $key = $this->key->getUnpackData([0 => $unpackData[$i]], 0);
                if (is_subclass_of($this->value, 'CoStruct')) {
                    $val = $this->value->getUnpackData([0 => $unpackData[$i + 1]], clone $this->value, 0);
                } else {
                    $val = $this->value->getUnpackData([0 => $unpackData[$i + 1]], 0);
                }
                
                $map[$key] = $val;
            }
            
            return $map;
        }
    }

    public function getCoType()
    {
        return CoType::CO_MAP;
    }
}

class CoStruct extends DataType
{   
    public function get(&$val, $tag, $isNeed = true)
    {
        $this->packVal($val, CoType::CO_STRUCT, $tag, $isNeed);
    }

    public function setVal($data)
    {   
        $data->pack($this->val);
        $bodyLen = strlen($this->val);
        $head = pack("N", $bodyLen);
        $this->val = $head.$this->val;
        return $this->val;
    }

    public function getUnpackData($data, $struct, $tag)
    {
        if (isset($data[$tag]) && $data[$tag]['type'] == CoType::CO_STRUCT) {
            $unpackData = coUnpack($data[$tag]['val']);
            $struct->unpack($unpackData);

            return $struct;
        }
    }

    public function getCoType()
    {
        return CoType::CO_STRUCT;
    }
}